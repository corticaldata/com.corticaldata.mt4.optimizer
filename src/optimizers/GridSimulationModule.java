package optimizers;

import controllers.Controller;
import controllers.VirtualController;
import expertadvisors.ExpertAdvisor;
import expertadvisors.GridExpertAdvisor;
import feedproviders.FeedProvider;
import feedproviders.SimulationFeedProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author paco@hernandezgomez.com
 */
public class GridSimulationModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(Controller.class).to(VirtualController.class);
		bind(FeedProvider.class).to(SimulationFeedProvider.class);
		bind(ExpertAdvisor.class).to(GridExpertAdvisor.class);
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new GridSimulationModule());
		FeedProvider feedProvider = injector.getInstance(FeedProvider.class);
		feedProvider.loadData();
		feedProvider.simulate();
	}
}
