package br.ufc.quixada.npi.gestaocompetencia.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import springfox.documentation.service.Tag;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig{

    @Bean
    public Docket apiDoc(List<SecurityScheme> authorizationTypes){
    	//TODO: VERIFICAR TODOS OS STATUS GLOBAIS
    	List<ResponseMessage> list = new ArrayList<>();
        list.add(new ResponseMessageBuilder().code(500).message("INTERNAL_SERVER_ERROR")
                .responseModel(new ModelRef("ExceptionResponse")).build());
        list.add(new ResponseMessageBuilder().code(401).message("UNAUTHORIZED")
                .responseModel(new ModelRef("ExceptionResponse")).build());
    	
        return new Docket(DocumentationType.SWAGGER_2)
        		.securitySchemes(Arrays.asList(new ApiKey("Token Authorization", "Authorization", "header")))
        		.securityContexts(Arrays.asList(securityContext()))
        	
        		.globalResponseMessage(RequestMethod.GET, list)
        		.globalResponseMessage(RequestMethod.POST, list)
        		.globalResponseMessage(RequestMethod.PUT, list)
        		.globalResponseMessage(RequestMethod.DELETE, list)
           		.ignoredParameterTypes(Usuario.class)
        		.tags(new Tag("Login", "REST API para efetuar login no sistema"),
        			  new Tag("Comportamento", "REST API para manipular os comportamentos"),
        			  new Tag("Inventário Comportamental", "REST API para manipular os inventários comportamentais"),
        			  new Tag("Inventário de Responsabilidades", "REST API para manipular os inventários de responsabilidades"),
        			  new Tag("Responsabilidades", "REST API para manipular as responsabilidades"),
        			  new Tag("Competencia", "REST API para manipular as competencias"),
        			  new Tag("Unidade", "REST API para manipular as unidades"))
                .select()
	                .apis(RequestHandlerSelectors.basePackage("br.ufc.quixada.npi.gestaocompetencia.controller"))
	                .paths(regex(".*"))
               .build();
    }
       
    private SecurityContext securityContext() {
    	return SecurityContext.builder()
    			.securityReferences(defaultAuth())
    			.forPaths(PathSelectors.regex("^(?!.*?(?:login)).*")) //Requisitar authorization de todos os paths exceto login 
    			.build();
    }
     
    private List<SecurityReference> defaultAuth(){
    	AuthorizationScope authorizationScope = new AuthorizationScope("global", "Autorização Global");
    	
    	AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    	authorizationScopes[0] = authorizationScope;
    	
    	return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

}
