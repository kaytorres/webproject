package sjsms.service;

import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.i18n.Messages;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SjSmsService implements ApplicationContextAware {
	private static ApplicationContext ctx;
	public static ApplicationContext getCtx() {
		return ctx;
		}
	public Object getServiceObject(AxisService axisService)  {
		Parameter springBeanName = axisService.getParameter("SpringBeanName");
		String beanName = ((String) springBeanName.getValue()).trim();
		if (beanName != null) {
			if (SjSmsService.getCtx() == null)
				System.out.print("applicationContext is NULL! ");
			if (SjSmsService.getCtx().getBean(beanName) == null)
				System.out.print("Axis2 Can't find Spring Bean: " + beanName);
			return SjSmsService.getCtx().getBean(beanName);
			} else {
				System.out.print(Messages.getMessage("paramIsNotSpecified",
						"SERVICE_SPRING_BEANNAME"));
				}
		return null;
		}
	public void setApplicationContext(ApplicationContext ctx)throws BeansException {
		SjSmsService.ctx = ctx;
		}
	}
