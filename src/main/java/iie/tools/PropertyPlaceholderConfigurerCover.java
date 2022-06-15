package iie.tools;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import sun.misc.BASE64Decoder;

public class PropertyPlaceholderConfigurerCover extends PropertyPlaceholderConfigurer {
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");
        byte[] bytes;
        String decodeUsername = "";
        String decodePassword = "";
		try {
			
			if (username != null) {
				System.out.println("11111111111111111111 [DEBUG] username: " + username);
                bytes = new BASE64Decoder().decodeBuffer(username);
                decodeUsername = new String(bytes, "UTF-8");
                System.out.println("11111111111111111111 [DEBUG] decodeUsername: " + decodeUsername);
				props.setProperty("jdbc.username", decodeUsername);
			}
			if (password != null) {
				System.out.println("22222222222222222222 [DEBUG] password: " + password);
                bytes = new BASE64Decoder().decodeBuffer(password);
                decodePassword = new String(bytes, "UTF-8");
                System.out.println("22222222222222222222 [DEBUG] decodePassword: " + decodePassword);
				props.setProperty("jdbc.password", decodePassword);
			}
			super.processProperties(beanFactory, props);
		} catch (Exception e) {
			System.out.println("33333333333333333333 [ERROR] PropertyPlaceholderConfigurerCover: " + e.toString());
		}
	}
}
