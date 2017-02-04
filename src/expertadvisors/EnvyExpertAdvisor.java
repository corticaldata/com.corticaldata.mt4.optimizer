package expertadvisors;

import controllers.Controller;

import java.util.Date;
import java.util.Random;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author paco@hernandezgomez.com
 */
@Singleton
public class EnvyExpertAdvisor extends ExpertAdvisor {
	
	//+------------------------------------------------------------------+
	//|                                                        Envy 1.13 |
	//|            Copyright © 2011-2017, Paco Hernández (@corticaldata) |
	//|                                          paco@hernandezgomez.com |
	//+------------------------------------------------------------------+
	
	@Inject
	public EnvyExpertAdvisor(Controller controller) {
		super(controller);
	}
	
	// 1.1
	// - Versión BASE
	
	// 1.2
	// - Simplificación, se reescribe entero, mucho más elegante y "mejó".
	// - Gestión del riesgo.
	// - Progresiones.
	
	// 1.4 (2014-03-02)
	// - Se quitan las señales de entrada. El sistema se va a basar en que no se tiene ni idea de que va
	//   a pasar en el futuro, salvo que los precios volverán en algún momento. Si se mira la gráfica, se 
	//   puede observar en cualquier par que los precios vuelven (al menos en parte) a largo plazo.
	// - Se irá a por pocos pips en un par para no arriesgar demasiado y se intentará combinar con muchos pares.
	
	// 1.8 (2014-03-02)
	// - Después de comprobar con la versión 1.4 que se puede tener una configuración holgada que funcione en
	//   todos los pares, se va a por una versión que haga lo siguiente:
	//   1. TAKE_PROFIT_PIPS se va a calcular en función del multiplicador para que siempre se ajuste la curva perfectamente.
	//	      Es decir, si el multiplicador es 2, el take_profit es igual al ancho del grid. 
	//	      Si el multiplicador es mayor de 2, el take_profit se disminuye ya que en siguientes operaciones no habrá que correr tanto para recuperar la inversión.
	//	      Si el multiplicador es menor de 2, el take_profit estará más alejado del ancho del grid.
	//   2. Es decir, se añade un TAKE_PROFIT_PERCENT y se ajustan los TAKE_PROFITS de las operaciones para que llegados a ese punto se gane el % establecido.
	//   3. Juraría que esto ya lo había implementado, pero no recuerdo donde.
	// - Se quita la señal aleatoria y siempre se está en el mercado en ambos sentidos.
	
	// 1.9 (2014-03-20)
	// - Se utilizan órdenes limitadas para hacerlo multipar.
	
	// 1.11 (2014-05-03)
	// - Venga, de hoy no pasa. Hago una prueba con multi-par y grid al estilo del Envy-Power (creo recordar).
	// - El primer tramo del grid es tontería porque abre en los dos sentidos. Es una pérdida de tiempo.
	
	// 1.12 (2014-06-10)
	// - Se corrige el tamaño mínimo de operación para que siga operando cuando se tiene un saldo pequeño.
	
	// 1.13 (2014-06-15)
	// - Se hace esta versión que en vez de abrir por un ancho de grid, abre por un periodo de tiempo. Así se trata de independizar un poco de los
	//   movimientos que haya y se fuerza un poco cerrar las series abiertas. También se está "inmunizado" contra movimientos grandes.
	
	public int BASE_HOUR = 16;
	public int PERIOD_HOURS = 6;
	public double TAKE_PROFIT_PIPS = 495;

	public double TAKE_PROFIT_PERCENT = 10;

	public double UNITS = 2.1;

	public double BASE_LOT_SIZE = 0.01;
	public double BASE_LOT_EACH = 10000;

	public int MAGIC_NUMBER = 7770121;
	public int MAX_SLIPPAGE_PIPS = 3;

	public String symbols[] = new String[] {"AUDUSD", "EURUSD", "GBPUSD", "NZDUSD", "USDCAD", "USDCHF"};

	double pointMultiplier;

	@Override
	public int init() {
		return (0);
	}
	
	@Override
	public int deinit() {
		return (0);
	}
	
	double baseBalance = 0;

	Random random = new Random(new Date().getTime());
	
	@Override
	public int start() {
		
		if (iBars(Symbol(), PERIOD_D1) <= 1)
			return(0);
		
		pointMultiplier = 0.0001 / MarketInfo(Symbol(), MODE_POINT);
		if (MarketInfo(Symbol(), MODE_DIGITS) < 4) pointMultiplier = 100.0 * pointMultiplier;
		
		if (OrdersTotal() == 0 || baseBalance == 0) {
			baseBalance = AccountBalance();
		}
		
		for (int s = 0; s < ArraySize(symbols); s++) {
		   
			int buyOrders = 0;
			int sellOrders = 0;
	      
			double buyNextUnits = 1;
			int buyNextIndex = 0;
			
			double sellNextUnits = 1;
			int sellNextIndex = 0;
			
			long buyOpenTime = 0;
			double buyOpenPrice = 0;
			double buyTakeProfit = NormalizeDouble(MarketInfo(symbols[s], MODE_ASK) + TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, MarketInfo(Symbol(), MODE_DIGITS));
			int buyPreviousIndex = (-1);
			
			long sellOpenTime = 0;
			double sellOpenPrice = 0;
			double sellTakeProfit = NormalizeDouble(MarketInfo(symbols[s], MODE_BID) - TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, MarketInfo(Symbol(), MODE_DIGITS));
			int sellPreviousIndex = (-1);
			
			for (int i = 0; i < OrdersTotal(); i++) {
				if (OrderSelect(i, SELECT_BY_POS)) {
					if (OrderMagicNumber() == MAGIC_NUMBER
							&& OrderSymbol() == symbols[s]) {
						
						if (OrderType() == OP_BUY) {
							buyOrders++;
							if (OrderOpenTime() > buyOpenTime) {
								buyOpenTime = OrderOpenTime();
								buyOpenPrice = OrderOpenPrice();
								buyTakeProfit = OrderTakeProfit();
								buyPreviousIndex = StrToInteger(OrderComment());
								
								buyNextIndex = buyPreviousIndex + 1;
								
								buyNextUnits = MathPow(UNITS, buyNextIndex);
							}
						}
						else if (OrderType() == OP_SELL) {
							sellOrders++;
							if (OrderOpenTime() > sellOpenTime) {
								sellOpenTime = OrderOpenTime();
								sellOpenPrice = OrderOpenPrice();
								sellTakeProfit = OrderTakeProfit();
								sellPreviousIndex = StrToInteger(OrderComment());
								
								sellNextIndex = sellPreviousIndex + 1;
								
								sellNextUnits = MathPow(UNITS, sellNextIndex);
							}
						}
					}
				}
			}
			
			for (int i = 0; i < OrdersTotal(); i++) {
				if (OrderSelect(i, SELECT_BY_POS)) {
					if (OrderMagicNumber() == MAGIC_NUMBER
							&& OrderSymbol() == symbols[s]) {
						if (OrderType() == OP_BUY) {
							if (OrderTakeProfit() != buyTakeProfit) {
								OrderModify(OrderTicket(), NormalizeDouble(OrderOpenPrice(), MarketInfo(Symbol(), MODE_DIGITS)), NormalizeDouble(OrderStopLoss(), MarketInfo(Symbol(), MODE_DIGITS)), NormalizeDouble(buyTakeProfit, MarketInfo(Symbol(), MODE_DIGITS)), 0);
							}
						}
						else if (OrderType() == OP_SELL) {
							if (OrderTakeProfit() != sellTakeProfit) {
								OrderModify(OrderTicket(), NormalizeDouble(OrderOpenPrice(), MarketInfo(Symbol(), MODE_DIGITS)), NormalizeDouble(OrderStopLoss(), MarketInfo(Symbol(), MODE_DIGITS)), NormalizeDouble(sellTakeProfit, MarketInfo(Symbol(), MODE_DIGITS)), 0);
							}
						}
					}
				}
			}
			
			if (buyOrders == 0 && sellOrders == 0) {
				if (random.nextBoolean()) {
					RefreshRates();
					OrderSend(symbols[s], OP_BUY, NormalizeDouble(MathMax(MarketInfo(symbols[s], MODE_MINLOT), AccountBalance() / BASE_LOT_EACH * BASE_LOT_SIZE * buyNextUnits), 2), MarketInfo(symbols[s], MODE_ASK), MAX_SLIPPAGE_PIPS * pointMultiplier, 0, MarketInfo(symbols[s], MODE_ASK) + TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, "" + buyNextIndex, MAGIC_NUMBER);
				}
				else {
					RefreshRates();
					OrderSend(symbols[s], OP_SELL, NormalizeDouble(MathMax(MarketInfo(symbols[s], MODE_MINLOT), AccountBalance() / BASE_LOT_EACH * BASE_LOT_SIZE * sellNextUnits), 2), MarketInfo(symbols[s], MODE_BID), MAX_SLIPPAGE_PIPS * pointMultiplier, 0, MarketInfo(symbols[s], MODE_BID) - TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, "" + sellNextIndex, MAGIC_NUMBER);
				}
			}
			
			if (buyOrders > 0 
					&& TimeCurrent() - buyOpenTime > 3600 * PERIOD_HOURS * 1000) {
				RefreshRates();
				OrderSend(symbols[s], OP_BUY, NormalizeDouble(MathMax(MarketInfo(symbols[s], MODE_MINLOT), AccountBalance() / BASE_LOT_EACH * BASE_LOT_SIZE * buyNextUnits), 2), MarketInfo(symbols[s], MODE_ASK), MAX_SLIPPAGE_PIPS * pointMultiplier, 0, MarketInfo(symbols[s], MODE_ASK) + TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, "" + buyNextIndex, MAGIC_NUMBER);
			}
	      
			if (sellOrders > 0 
					&& TimeCurrent() - sellOpenTime > 3600 * PERIOD_HOURS * 1000) {
				RefreshRates();
				OrderSend(symbols[s], OP_SELL, NormalizeDouble(MathMax(MarketInfo(symbols[s], MODE_MINLOT), AccountBalance() / BASE_LOT_EACH * BASE_LOT_SIZE * sellNextUnits), 2), MarketInfo(symbols[s], MODE_BID), MAX_SLIPPAGE_PIPS * pointMultiplier, 0, MarketInfo(symbols[s], MODE_BID) - TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, "" + sellNextIndex, MAGIC_NUMBER);
			}
		}
		
		if ((AccountEquity() - baseBalance) * 100 / baseBalance >= TAKE_PROFIT_PERCENT) {
			for (int i = OrdersTotal() - 1; i >= 0; i--) {
				if (OrderSelect(i, SELECT_BY_POS)) {
					if (OrderMagicNumber() == MAGIC_NUMBER) {
						OrderClose(OrderTicket(), OrderLots(), OrderClosePrice(), MAX_SLIPPAGE_PIPS * pointMultiplier);
					}
				}
			}
		}
		
		return (0);
	}
}
