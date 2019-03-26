package com.yibai.crown.web.configuration;

import brave.Tracing;
import com.google.common.collect.Lists;
import com.yibai.crown.base.component.ReloadableMessageSource;
import com.yibai.crown.web.advisor.RequestBodyAdvisor;
import com.yibai.crown.web.advisor.ResponseBodyAdvisor;
import com.yibai.crown.web.interceptor.DefaultHandlerInterceptor;
import com.yibai.crown.web.resolver.CustomHandlerExceptionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;


@Configuration
public class WebMvcConfiguration extends DelegatingWebMvcConfiguration {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/"
    };

    @Autowired
    private ReloadableMessageSource reloadableMessageSource;

    @Override
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
        adapter.setRequestBodyAdvice(Lists.newArrayList(new RequestBodyAdvisor()));
        adapter.setResponseBodyAdvice(Lists.newArrayList(new ResponseBodyAdvisor()));
        return adapter;
    }

//    @Override
//    @Autowired(required = false)
//    public void setConfigurers(List<WebMvcConfigurer> configurers) {
//        if (configurers == null) {
//            configurers = new ArrayList<>();
//        }
//        configurers.add(webMvcConfigurer());
//        super.setConfigurers(configurers);
//    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultHandlerExceptionResolver defaultHandlerExceptionResolver() {
        return new CustomHandlerExceptionResolver(reloadableMessageSource);
    }

    @Bean
    public HandlerInterceptor defaultHandlerInterceptor(Tracing tracing) {
        return new DefaultHandlerInterceptor(tracing);
    }

//    @Bean
//    public KickOffInterceptor kickOffInterceptor() {
//        return new KickOffInterceptor();
//    }

//    @Bean
//    @ConditionalOnBean({AccessRecorder.class})
//    public HandlerInterceptor accessRecordInterceptor(AccessRecorder recorder) {
//        return new AccessRecordInterceptor(recorder);
//    }

//    @Bean
//    @ConditionalOnBean(AuthorizingSecurityManager.class)
//    public HttpTracing httpTracing(Tracing tracing) {
//        return HttpTracing.newBuilder(tracing)
//                .clientParser(new HttpClientParser() {
//                    @Override
//                    public <Req> void request(HttpAdapter<Req, ?> adapter, Req req, SpanCustomizer customizer) {
//                        super.request(adapter, req, customizer);
//                    }
//                })
//                .serverParser(new HttpServerParser() {
//                    @Override
//                    public <Req> void request(HttpAdapter<Req, ?> adapter, Req req, SpanCustomizer customizer) {
//                        super.request(adapter, req, customizer);
//                        Subject subject = SecurityUtils.getSubject();
//                        Session session = subject.getSession(false);
//                        if (session != null) {
//                            UserAuthPrincipal principal = (UserAuthPrincipal) subject.getPrincipal();
//                            customizer.tag("userId", principal.getUserId());
//                            customizer.tag("sessionId", session.getId().toString());
//                        }
//                    }
//                })
//                .clientSampler(HttpSampler.TRACE_ID)
//                .serverSampler(HttpSampler.TRACE_ID)
//                .build();
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    public AsyncHandlerInterceptor webServerTraceInterceptor(HttpTracing tracing) {
//        return TracingHandlerInterceptor.create(tracing);
//    }



}
