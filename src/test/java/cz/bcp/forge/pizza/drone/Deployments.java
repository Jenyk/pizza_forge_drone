package cz.bcp.forge.pizza.drone;

import java.io.File;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jboss.shrinkwrap.resolver.api.maven.filter.ScopeFilter;

public class Deployments {

	private static final String WEBAPP_SRC = "src/main/webapp";

	public static WebArchive createDeployment() {
	

		WebArchive war = ShrinkWrap.create(WebArchive.class)
				.addPackages(false, "cz.bcp.forge.pizza.model", "cz.bcp.forge.pizza.view")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/validation.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
    		        .importDirectory(WEBAPP_SRC).as(GenericArchive.class)    		        
    		     // ,  "/", Filters.include(".*\\.xhtml$")
    		        )
                
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).includeDependenciesFromPom("pom.xml")
                		.resolveAs(GenericArchive.class, new ScopeFilter("runtime")))
                .setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"));
		
		System.err.println(war.toString(true));
		return war;
	}
}
