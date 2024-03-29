$.fn.ajaxSubmit = function(options) {
 // fast fail if nothing selected (http://dev.jquery.com/ticket/2752)
 if (!this.length) {
  log('ajaxSubmit: skipping submit process - no element selected');
  return this;
 }

 if (typeof options == 'function')
  options = { success: options };

 var url = $.trim(this.attr('action'));
 if (url) {
  // clean url (don't include hash vaue)
  url = (url.match(/^([^#]+)/)||[])[1];
    }
    url = url || window.location.href || '';

 options = $.extend({
  url:  url,
  type: this.attr('method') || 'GET'
 }, options || {});

 // hook for manipulating the form data before it is extracted;
 // convenient for use with rich editors like tinyMCE or FCKEditor
 var veto = {};
 this.trigger('form-pre-serialize', [this, options, veto]);
 if (veto.veto) {
  log('ajaxSubmit: submit vetoed via form-pre-serialize trigger');
  return this;
 }

 // provide opportunity to alter form data before it is serialized
 if (options.beforeSerialize && options.beforeSerialize(this, options) === false) {
  log('ajaxSubmit: submit aborted via beforeSerialize callback');
  return this;
 }

 var a = this.formToArray(options.semantic);
 if (options.data) {
  options.extraData = options.data;
  for (var n in options.data) {
    if(options.data[n] instanceof Array) {
   for (var k in options.data[n])
     a.push( { name: n, value: options.data[n][k] } );
    }
    else
    a.push( { name: n, value: options.data[n] } );
  }
 }

 // give pre-submit callback an opportunity to abort the submit
 if (options.beforeSubmit && options.beforeSubmit(a, this, options) === false) {
  log('ajaxSubmit: submit aborted via beforeSubmit callback');
  return this;
 }

 // fire vetoable 'validate' event
 this.trigger('form-submit-validate', [a, this, options, veto]);
 if (veto.veto) {
  log('ajaxSubmit: submit vetoed via form-submit-validate trigger');
  return this;
 }

 var q = $.param(a);

 if (options.type.toUpperCase() == 'GET') {
  options.url += (options.url.indexOf('?') >= 0 ? '&' : '?') + q;
  options.data = null;  // data is null for 'get'
 }
 else
  options.data = q; // data is the query string for 'post'

 var $form = this, callbacks = [];
 if (options.resetForm) callbacks.push(function() { $form.resetForm(); });
 if (options.clearForm) callbacks.push(function() { $form.clearForm(); });

 // perform a load on the target only if dataType is not provided
 if (!options.dataType && options.target) {
  var oldSuccess = options.success || function(){};
  callbacks.push(function(data) {
   $(options.target).html(data).each(oldSuccess, arguments);
  });
 }
 else if (options.success)
  callbacks.push(options.success);

 options.success = function(data, status) {
  for (var i=0, max=callbacks.length; i < max; i++)
   callbacks[i].apply(options, [data, status, $form]);
 };

 // are there files to upload?
 var files = $('input:file', this).fieldValue();
 var found = false;
 for (var j=0; j < files.length; j++)
  if (files[j])
   found = true;

 var multipart = false;
// var mp = 'multipart/form-data';
// multipart = ($form.attr('enctype') == mp || $form.attr('encoding') == mp);

 // options.iframe allows user to force iframe mode
   if (options.iframe || found || multipart) {
    // hack to fix Safari hang (thanks to Tim Molendijk for this)
    // see:  http://groups.google.com/group/jquery-dev/browse_thread/thread/36395b7ab510dd5d
    if (options.closeKeepAlive)
     $.get(options.closeKeepAlive, fileUpload);
    else
     fileUpload();
    }
   else
    $.ajax(options);

 // fire 'notify' event
 this.trigger('form-submit-notify', [this, options]);
 return this;


 // private function for handling file uploads (hat tip to YAHOO!)
 function fileUpload() {
  var form = $form[0];

  if ($(':input[name=submit]', form).length) {
   alert('Error: Form elements must not be named "submit".');
   return;
  }

  var opts = $.extend({}, $.ajaxSettings, options);
  var s = $.extend(true, {}, $.extend(true, {}, $.ajaxSettings), opts);

  var id = 'jqFormIO' + (new Date().getTime());
  var $io = $('<iframe id="' + id + '" name="' + id + '" src="about:blank" />');
  var io = $io[0];

  $io.css({ position: 'absolute', top: '-1000px', left: '-1000px' });

  var xhr = { // mock object
   aborted: 0,
   responseText: null,
   responseXML: null,
   status: 0,
   statusText: 'n/a',
   getAllResponseHeaders: function() {},
   getResponseHeader: function() {},
   setRequestHeader: function() {},
   abort: function() {
    this.aborted = 1;
    $io.attr('src','about:blank'); // abort op in progress
   }
  };

  var g = opts.global;
  // trigger ajax global events so that activity/block indicators work like normal
  if (g && ! $.active++) $.event.trigger("ajaxStart");
  if (g) $.event.trigger("ajaxSend", [xhr, opts]);

  if (s.beforeSend && s.beforeSend(xhr, s) === false) {
   s.global && $.active--;
   return;
  }
  if (xhr.aborted)
   return;

  var cbInvoked = 0;
  var timedOut = 0;

  // add submitting element to data if we know it
  var sub = form.clk;
  if (sub) {
   var n = sub.name;
   if (n && !sub.disabled) {
    options.extraData = options.extraData || {};
    options.extraData[n] = sub.value;
    if (sub.type == "image") {
     options.extraData[name+'.x'] = form.clk_x;
     options.extraData[name+'.y'] = form.clk_y;
    }
   }
  }

  // take a breath so that pending repaints get some cpu time before the upload starts
  setTimeout(function() {
   // make sure form attrs are set
   var t = $form.attr('target'), a = $form.attr('action');

   // update form attrs in IE friendly way
   form.setAttribute('target',id);
   if (form.getAttribute('method') != 'POST')
    form.setAttribute('method', 'POST');
   if (form.getAttribute('action') != opts.url)
    form.setAttribute('action', opts.url);

   // ie borks in some cases when setting encoding
   if (! options.skipEncodingOverride) {
    $form.attr({
     encoding: 'multipart/form-data',
     enctype:  'multipart/form-data'
    });
   }

   // support timout
   if (opts.timeout)
    setTimeout(function() { timedOut = true; cb(); }, opts.timeout);

   // add "extra" data to form if provided in options
   var extraInputs = [];
   try {
    if (options.extraData)
     for (var n in options.extraData)
      extraInputs.push(
       $('<input type="hidden" name="'+n+'" value="'+options.extraData[n]+'" />')
        .appendTo(form)[0]);

    // add iframe to doc and submit the form
    $io.appendTo('body');
    io.attachEvent ? io.attachEvent('onload', cb) : io.addEventListener('load', cb, false);
    form.submit();
   }
   finally {
    // reset attrs and remove "extra" input elements
    form.setAttribute('action',a);
    t ? form.setAttribute('target', t) : $form.removeAttr('target');
    $(extraInputs).remove();
   }
  }, 10);

  var domCheckCount = 50;

  function cb() {
   if (cbInvoked++) return;

   io.detachEvent ? io.detachEvent('onload', cb) : io.removeEventListener('load', cb, false);

   var ok = true;
   try {
    if (timedOut) throw 'timeout';
    // extract the server response from the iframe
    var data, doc;

    doc = io.contentWindow ? io.contentWindow.document : io.contentDocument ? io.contentDocument : io.document;
    
    var isXml = opts.dataType == 'xml' || doc.XMLDocument || $.isXMLDoc(doc);
    log('isXml='+isXml);
    if (!isXml && (doc.body == null || doc.body.innerHTML == '')) {
      if (--domCheckCount) {
      // in some browsers (Opera) the iframe DOM is not always traversable when
      // the onload callback fires, so we loop a bit to accommodate
      cbInvoked = 0;
      setTimeout(cb, 100);
      return;
     }
     log('Could not access iframe DOM after 50 tries.');
     return;
    }

    xhr.responseText = doc.body ? doc.body.innerHTML : null;
    xhr.responseXML = doc.XMLDocument ? doc.XMLDocument : doc;
    xhr.getResponseHeader = function(header){
     var headers = {'content-type': opts.dataType};
     return headers[header];
    };

    if (opts.dataType == 'json' || opts.dataType == 'script') {
     // see if user embedded response in textarea
     var ta = doc.getElementsByTagName_r('textarea')[0];
     if (ta)
      xhr.responseText = ta.value;
     else {
      // account for browsers injecting pre around json response
      var pre = doc.getElementsByTagName_r('pre')[0];
      if (pre)
       xhr.responseText = pre.innerHTML;
     }    
    }
    else if (opts.dataType == 'xml' && !xhr.responseXML && xhr.responseText != null) {
     xhr.responseXML = toXml(xhr.responseText);
    }
    data = $.httpData(xhr, opts.dataType);
   }
   catch(e){
    ok = false;
    $.handleError(opts, xhr, 'error', e);
   }

   // ordering of these callbacks/triggers is odd, but that's how $.ajax does it
   if (ok) {
    opts.success(data, 'success');
    if (g) $.event.trigger("ajaxSuccess", [xhr, opts]);
   }
   if (g) $.event.trigger("ajaxComplete", [xhr, opts]);
   if (g && ! --$.active) $.event.trigger("ajaxStop");
   if (opts.complete) opts.complete(xhr, ok ? 'success' : 'error');

   // clean up
   setTimeout(function() {
    $io.remove();
    xhr.responseXML = null;
   }, 100);
  };

  function toXml(s, doc) {
   if (window.ActiveXObject) {
    doc = new ActiveXObject('Microsoft.XMLDOM');
    doc.async = 'false';
    doc.loadXML(s);
   }
   else
    doc = (new DOMParser()).parseFromString(s, 'text/xml');
   return (doc && doc.documentElement && doc.documentElement.tagName != 'parsererror') ? doc : null;
  };
  
 };
};


$.fn.ajaxForm = function(options) {
 return this.ajaxFormUnbind().bind('submit.form-plugin', function() {
  $(this).ajaxSubmit(options);
  return false;
 }).bind('click.form-plugin', function(e) {
  var $el = $(e.target);
  if (!($el.is(":submit,input:image"))) {
   return;
  }
  var form = this;
  form.clk = e.target;
  if (e.target.type == 'image') {
   if (e.offsetX != undefined) {
    form.clk_x = e.offsetX;
    form.clk_y = e.offsetY;
   } else if (typeof $.fn.offset == 'function') { // try to use dimensions plugin
    var offset = $el.offset();
    form.clk_x = e.pageX - offset.left;
    form.clk_y = e.pageY - offset.top;
   } else {
    form.clk_x = e.pageX - e.target.offsetLeft;
    form.clk_y = e.pageY - e.target.offsetTop;
   }
  }
  // clear form vars
  setTimeout(function() { form.clk = form.clk_x = form.clk_y = null; }, 10);
 });
};

// ajaxFormUnbind unbinds the event handlers that were bound by ajaxForm
$.fn.ajaxFormUnbind = function() {
 return this.unbind('submit.form-plugin click.form-plugin');
};


$.fn.formToArray = function(semantic) {
 var a = [];
 if (this.length == 0) return a;

 var form = this[0];
 var els = semantic ? form.getElementsByTagName_r('*') : form.elements;
 if (!els) return a;
 for(var i=0, max=els.length; i < max; i++) {
  var el = els[i];
  var n = el.name;
  if (!n) continue;

  if (semantic && form.clk && el.type == "image") {
   // handle image inputs on the fly when semantic == true
   if(!el.disabled && form.clk == el) {
    a.push({name: n, value: $(el).val()});
    a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
   }
   continue;
  }

  var v = $.fieldValue(el, true);
  if (v && v.constructor == Array) {
   for(var j=0, jmax=v.length; j < jmax; j++)
    a.push({name: n, value: v[j]});
  }
  else if (v !== null && typeof v != 'undefined')
   a.push({name: n, value: v});
 }

 if (!semantic && form.clk) {
  // input type=='image' are not found in elements array! handle it here
  var $input = $(form.clk), input = $input[0], n = input.name;
  if (n && !input.disabled && input.type == 'image') {
   a.push({name: n, value: $input.val()});
   a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
  }
 }
 return a;
};


$.fn.formSerialize = function(semantic) {
 //hand off to jQuery.param for proper encoding
 return $.param(this.formToArray(semantic));
};


$.fn.fieldSerialize = function(successful) {
 var a = [];
 this.each(function() {
  var n = this.name;
  if (!n) return;
  var v = $.fieldValue(this, successful);
  if (v && v.constructor == Array) {
   for (var i=0,max=v.length; i < max; i++)
    a.push({name: n, value: v[i]});
  }
  else if (v !== null && typeof v != 'undefined')
   a.push({name: this.name, value: v});
 });
 //hand off to jQuery.param for proper encoding
 return $.param(a);
};


$.fn.fieldValue = function(successful) {
 for (var val=[], i=0, max=this.length; i < max; i++) {
  var el = this[i];
  var v = $.fieldValue(el, successful);
  if (v === null || typeof v == 'undefined' || (v.constructor == Array && !v.length))
   continue;
  v.constructor == Array ? $.merge(val, v) : val.push(v);
 }
 return val;
};


$.fieldValue = function(el, successful) {
 var n = el.name, t = el.type, tag = el.tagName.toLowerCase();
 if (typeof successful == 'undefined') successful = true;

 if (successful && (!n || el.disabled || t == 'reset' || t == 'button' ||
  (t == 'checkbox' || t == 'radio') && !el.checked ||
  (t == 'submit' || t == 'image') && el.form && el.form.clk != el ||
  tag == 'select' && el.selectedIndex == -1))
   return null;

 if (tag == 'select') {
  var index = el.selectedIndex;
  if (index < 0) return null;
  var a = [], ops = el.options;
  var one = (t == 'select-one');
  var max = (one ? index+1 : ops.length);
  for(var i=(one ? index : 0); i < max; i++) {
   var op = ops[i];
   if (op.selected) {
    var v = op.value;
    if (!v) // extra pain for IE...
     v = (op.attributes && op.attributes['value'] && !(op.attributes['value'].specified)) ? op.text : op.value;
    if (one) return v;
    a.push(v);
   }
  }
  return a;
 }
 return el.value;
};


$.fn.clearForm = function() {
 return this.each(function() {
  $('input,select,textarea', this).clearFields();
 });
};


$.fn.clearFields = $.fn.clearInputs = function() {
 return this.each(function() {
  var t = this.type, tag = this.tagName.toLowerCase();
  if (t == 'text' || t == 'password' || tag == 'textarea')
   this.value = '';
  else if (t == 'checkbox' || t == 'radio')
   this.checked = false;
  else if (tag == 'select')
   this.selectedIndex = -1;
 });
};


$.fn.resetForm = function() {
 return this.each(function() {
  // guard against an input with the name of 'reset'
  // note that IE reports the reset function as an 'object'
  if (typeof this.reset == 'function' || (typeof this.reset == 'object' && !this.reset.nodeType))
   this.reset();
 });
};


$.fn.enable = function(b) {
 if (b == undefined) b = true;
 return this.each(function() {
  this.disabled = !b;
 });
};


$.fn.selected = function(select) {
 if (select == undefined) select = true;
 return this.each(function() {
  var t = this.type;
  if (t == 'checkbox' || t == 'radio')
   this.checked = select;
  else if (this.tagName.toLowerCase() == 'option') {
   var $sel = $(this).parent('select');
   if (select && $sel[0] && $sel[0].type == 'select-one') {
    // deselect all other options
    $sel.find('option').selected(false);
   }
   this.selected = select;
  }
 });
};

// helper fn for console logging
// set $.fn.ajaxSubmit.debug to true to enable debug logging
function log() {
 if ($.fn.ajaxSubmit.debug && window.console && window.console.log)
  window.console.log('[jquery.form] ' + Array.prototype.join.call(arguments,''));
};