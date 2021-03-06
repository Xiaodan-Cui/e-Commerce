package xiaodan.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import xiaodan.ecommerce.entity.Country;
import xiaodan.ecommerce.entity.Product;
import xiaodan.ecommerce.entity.ProductCategory;
import xiaodan.ecommerce.entity.State;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager ){
        entityManager = theEntityManager;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions={HttpMethod.DELETE,HttpMethod.POST,HttpMethod.PUT};
        //disabled HTTP methods
        disabledHTTPMethods(Product.class,config, theUnsupportedActions);
        disabledHTTPMethods(ProductCategory.class,config, theUnsupportedActions);
        disabledHTTPMethods(Country.class,config, theUnsupportedActions);
        disabledHTTPMethods(State.class,config, theUnsupportedActions);

        exposeId(config);
    }

    private void disabledHTTPMethods(Class theClass,RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));
    }

    private void exposeId(RepositoryRestConfiguration config){
        Set<EntityType<?>> entities=entityManager.getMetamodel().getEntities();
        List<Class> entityClasses= new ArrayList<>();

        for(EntityType tempEntityType :entities){
            entityClasses.add(tempEntityType.getJavaType());
        }
        Class[] domainTypes=entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }





}
