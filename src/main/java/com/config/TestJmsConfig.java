package com;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jndi.JndiTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
@EnableJms
public class TestJmsConfig {


        @Value("${weblogic.jms.url}")
        private String url;


        @Value("${weblogic.jms.connectionFactoryName}")
        private String connectionFactoryName;

        @Value("${weblogic.jms.queue}")
        private String mpiResponseQueue;


        private Properties getJNDiProperties() {

            final Properties jndiProps = new Properties();
            jndiProps.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
            jndiProps.setProperty(Context.PROVIDER_URL, url);
            //https://docs.oracle.com/en/middleware/standalone/weblogic-server/14.1.1.0/wlapi/weblogic/jndi/WLContext.html
            jndiProps.setProperty("weblogic.jndi.connectTimeout", "30000");
            jndiProps.setProperty("weblogic.jndi.responseReadTimeout", "30000");

            return jndiProps;

        }

        /**
         * Create connection factory.
         *
         * @return
         */
        @Bean
        public ConnectionFactory queueConnectionFactory() {
            // JNDI connection factory name stored in weblogic.
            return lookupByJndiTemplate(connectionFactoryName, QueueConnectionFactory.class);
        }

        /**
         * Create InitialContext.
         *
         * @return
         */
        @Bean
        public JndiTemplate jndiTemplate() {
            JndiTemplate jndiTemplate = new JndiTemplate();

            jndiTemplate.setEnvironment(getJNDiProperties());

            return jndiTemplate;
        }

        @Bean
        public Destination mpiResponseQueue() {
            return lookupByJndiTemplate(mpiResponseQueue, Destination.class);
        }
        /**
         *
         * @param jndiName
         * @param requiredType
         * @return
         */
        @SuppressWarnings("unchecked")
        protected <T> T lookupByJndiTemplate(String jndiName, Class<T> requiredType) {

            try {
                Object located = jndiTemplate().lookup(jndiName);
                if (located == null) {
                    throw new NameNotFoundException("JNDI object with [" + jndiName + "] not found");
                }
                return (T) located;
            } catch (NamingException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         *
         * @param jndiName
         * @param requiredType
         * @return
         */
        @SuppressWarnings("unchecked")
        protected final <T> T lookup(String jndiName, Class<T> requiredType) {

            try {
                InitialContext initialContext = new InitialContext(getJNDiProperties());
                Object located = initialContext.lookup(jndiName);
                if (located == null) {
                    throw new NameNotFoundException("JNDI object with [" + jndiName + "] not found");
                }
                return (T) located;
            } catch (NamingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

