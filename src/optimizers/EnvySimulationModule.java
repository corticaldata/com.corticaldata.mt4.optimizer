package optimizers;

import controllers.Controller;
import controllers.VirtualController;
import expertadvisors.ExpertAdvisor;
import expertadvisors.EnvyExpertAdvisor;
import feedproviders.FeedProvider;
import feedproviders.SimulationFeedProvider;

import java.util.Date;
import java.util.Random;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author paco@hernandezgomez.com
 */
public class EnvySimulationModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(Controller.class).to(VirtualController.class);
		bind(FeedProvider.class).to(SimulationFeedProvider.class);
		bind(ExpertAdvisor.class).to(EnvyExpertAdvisor.class);
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new EnvySimulationModule());
		FeedProvider feedProvider = injector.getInstance(FeedProvider.class);
		feedProvider.loadData();

		Random random = new Random(new Date().getTime());
		
		int count = 0;
		
		while (true) {
			count++;
			System.out.println(count);
			EnvyExpertAdvisor ea = injector.getInstance(EnvyExpertAdvisor.class);
			
			ea.BASE_HOUR = Math.abs(random.nextInt()) % 24;
			ea.PERIOD_HOURS = Math.abs(random.nextInt()) % 72 + 1;
			ea.TAKE_PROFIT_PIPS = Math.abs(random.nextInt()) % 300 + 1;
			ea.TAKE_PROFIT_PERCENT = Math.abs(random.nextInt()) % 100 + 1;
			ea.UNITS = random.nextDouble() * 3;
			ea.BASE_LOT_SIZE = (random.nextBoolean() ? 0.01 : 0.1);
			ea.BASE_LOT_EACH = 1000;
			
			feedProvider.simulate();
			
			VirtualController controller = injector.getInstance(VirtualController.class);
			
			if (controller.accountEquity() > 0 && controller.accountBalance() > 0 || controller.maxEquity > 100000) {
				System.out.println("-----------------------------------------------------------");

				System.out.println("Equity: " + controller.accountEquity());
				System.out.println("Balance: " + controller.accountBalance());
				System.out.println("Max. equity: " + controller.maxEquity);
				System.out.println("Max drawdown (%): " + controller.maxDrawdownPercent);
				System.out.println("BASE_HOUR: " + ea.BASE_HOUR);
				System.out.println("PERIOD_HOURS: " + ea.PERIOD_HOURS);
				System.out.println("TAKE_PROFIT_PIPS: " + ea.TAKE_PROFIT_PIPS);
				System.out.println("TAKE_PROFIT_PERCENT: " + ea.TAKE_PROFIT_PERCENT);
				System.out.println("UNITS: " + ea.UNITS);
				System.out.println("BASE_LOT_SIZE: " + ea.BASE_LOT_SIZE);
				System.out.println(controller.accountEquity() + ", " + controller.accountBalance());
			}
		}
	}
}
