package com.letschat.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.letschat.handlers.CustomLoginSuccessHandler;
import com.letschat.handlers.CustomLogoutHandler;
import com.letschat.repository.ReactiveUserAccountRepository;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer,ApplicationContextAware{
	
	private ApplicationContext ctx;
	
		@Autowired
	  private ReactiveUserAccountRepository userRepository;
	
	
	  @Bean
	    public SpringResourceTemplateResolver thymeleafTemplateResolver() {

	        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
	        resolver.setApplicationContext(this.ctx);
	        resolver.setPrefix("classpath:/templates/");
	        resolver.setSuffix(".html");
	        resolver.setTemplateMode(TemplateMode.HTML);
	        resolver.setCacheable(false);
	        resolver.setCheckExistence(false);
	        return resolver;

	    }

	    @Bean
	    public ISpringWebFluxTemplateEngine thymeleafTemplateEngine() {
	        // We override here the SpringTemplateEngine instance that would otherwise be
	        // instantiated by
	        // Spring Boot because we want to apply the SpringWebFlux-specific context
	        // factory, link builder...
	        final SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
	        templateEngine.setTemplateResolver(thymeleafTemplateResolver());
	        return templateEngine;
	    }
	    

	    @Bean
	    public ThymeleafReactiveViewResolver thymeleafChunkedAndDataDrivenViewResolver() {
	        final ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
	        viewResolver.setTemplateEngine(thymeleafTemplateEngine());
//	        viewResolver.setOrder(1);
//	        viewResolver.setViewNames(new String[]{"home"});
	        viewResolver.setResponseMaxChunkSizeBytes(8192); // OUTPUT BUFFER size limit
	        return viewResolver;
	    }

	    @Override
	    public void configureViewResolvers(ViewResolverRegistry registry) {
	        registry.viewResolver(thymeleafChunkedAndDataDrivenViewResolver());
	    }
	
	
	
	
	
    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf().disable()
            .authorizeExchange()
                .pathMatchers("/login", "/logout","/signup").permitAll()
                .pathMatchers("/i18n/**",
                    "/css/**",
                    "/public/**",
                    "/fonts/**",
                    "/icons-reference/**",
                    "/img/**",
                    "/js/**","/favicon.*",
                    "/vendor/**","/react/signup","/templates/**","/resources/**","/static/**","classpath:/resources/**").permitAll()
           .anyExchange()
                .authenticated()
               /* .and()
                .oauth2Login()*/
                
          
                .and()
            .formLogin()
                .loginPage("/login")
                .authenticationSuccessHandler(new CustomLoginSuccessHandler())
                .authenticationFailureHandler((webFilterExchange,exception) ->{
                	return Mono.error(exception);
                })
                
                .and()
            .logout()
            .logoutHandler(new CustomLogoutHandler())
                
                .and()
           .build();
        
     
    }


  /*  @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
		
	}
	
	   @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler(
	            "/webjars/**",
	            "/img/**",
	            "/css/**",
	            "/js/**",
	            "/public/**")
	            .addResourceLocations(
	                    "classpath:/META-INF/resources/webjars/",
	                    "classpath:/static/img/",
	                    "classpath:/static/css/",
	                    "classpath:/static/js/",
	                    "classpath:/public/");
	     }
    
	   
	   @Bean
	   public ReactiveUserDetailsService  userDetailsService() {
	     return (username) -> userRepository.findByUsername(username);
	   }
	   
	   @Bean
	   public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepo,ServerOAuth2AuthorizedClientRepository authorizedClientRepo) {
	   ServerOAuth2AuthorizedClientExchangeFilterFunction filter = 
	   new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepo, authorizedClientRepo);
	 
	   return WebClient.builder().filter(filter).build();
	   }
    

    
}
