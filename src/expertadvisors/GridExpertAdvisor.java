package expertadvisors;

import controllers.Controller;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author paco@hernandezgomez.com
 */
@Singleton
public class GridExpertAdvisor extends ExpertAdvisor {
	
	//+------------------------------------------------------------------+
	//|                                                  Simple Grid 1.2 |
	//|            Copyright © 2013-2017, Paco Hernández (@corticaldata) |
	//|                                          paco@hernandezgomez.com |
	//+------------------------------------------------------------------+
	
	@Inject
	public GridExpertAdvisor(Controller controller) {
		super(controller);
	}
	
	final double VERSION = 1.2;
	
	public double UNITS = 2.0;
	public int STEPS = 1000;
	
	// Limits configuration
	public int MAX_SLIPPAGE_PIPS = 3;
	
	public int MAGIC_NUMBER = 7770121;
	public double BASE_LOT_SIZE = 0.01;
	public double BASE_LOT_EACH = 100000;
	public double TAKE_PROFIT_PIPS = 1300.0;
	
	double pointMultiplier;
	
	@Override
	public int init() {
		return (0);
	}
	
	@Override
	public int deinit() {
		return (0);
	}
	
	@Override
	public int start() {
		
		if (iBars(Symbol(), PERIOD_D1) <= 1)
			return(0);
		
		pointMultiplier = 0.0001 / MarketInfo(Symbol(), MODE_POINT);
		if (MarketInfo(Symbol(), MODE_DIGITS) < 4) pointMultiplier = 100.0 * pointMultiplier;
		
		int signal = 0;
		
		if (MarketInfo(Symbol(), MODE_BID) > iClose(Symbol(), PERIOD_D1, 1))
			signal = 1;
		else
			signal = (-1);
		
		int buyOrders = 0;
		int sellOrders = 0;
		
		double buyNextUnits = 1;
		int buyNextIndex = 0;
		int buyNextStep = STEPS;
		
		double sellNextUnits = 1;
		int sellNextIndex = 0;
		int sellNextStep = STEPS;
		
		long buyOpenTime = 0;
		double buyOpenPrice = 0;
		double buyTakeProfit = NormalizeDouble(MarketInfo(Symbol(), MODE_ASK) + TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, MarketInfo(Symbol(), MODE_DIGITS));
		int buyPreviousIndex = (-1);
		
		long sellOpenTime = 0;
		double sellOpenPrice = 0;
		double sellTakeProfit = NormalizeDouble(MarketInfo(Symbol(), MODE_BID) - TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, MarketInfo(Symbol(), MODE_DIGITS));
		int sellPreviousIndex = (-1);
		
		for (int i = 0; i < OrdersTotal(); i++) {
			if (OrderSelect(i, SELECT_BY_POS)) {
				if (OrderMagicNumber() == MAGIC_NUMBER && OrderSymbol().equals(Symbol())) {
					
					if (OrderType() == OP_BUY) {
						buyOrders++;
						if (OrderOpenTime() > buyOpenTime) {
							buyOpenTime = OrderOpenTime();
							buyOpenPrice = OrderOpenPrice();
							buyTakeProfit = OrderTakeProfit();
							buyPreviousIndex = StrToInteger(OrderComment());
							
							if (OrderProfit() + OrderSwap() - OrderCommission() >= 0)
								buyNextIndex = 0;
							else
								buyNextIndex = buyPreviousIndex + 1;
							
							buyNextUnits = MathPow(UNITS, buyNextIndex);
							buyNextStep = STEPS;
						}
					} 
					
					else if (OrderType() == OP_SELL) {
						sellOrders++;
						if (OrderOpenTime() > sellOpenTime) {
							sellOpenTime = OrderOpenTime();
							sellOpenPrice = OrderOpenPrice();
							sellTakeProfit = OrderTakeProfit();
							sellPreviousIndex = StrToInteger(OrderComment());
							
							if (OrderProfit() + OrderSwap() - OrderCommission() >= 0)
								sellNextIndex = 0;
							else
								sellNextIndex = sellPreviousIndex + 1;
							
							sellNextUnits = MathPow(UNITS, sellNextIndex);
							sellNextStep = STEPS;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < OrdersTotal(); i++) {
			if (OrderSelect(i, SELECT_BY_POS)) {
				if (OrderMagicNumber() == MAGIC_NUMBER && OrderSymbol().equals(Symbol())) {
					if (OrderType() == OP_BUY) {
						if (OrderTakeProfit() != buyTakeProfit) {
							OrderModify(OrderTicket(),
									NormalizeDouble(OrderOpenPrice(), MarketInfo(Symbol(), MODE_DIGITS)),
									NormalizeDouble(OrderStopLoss(), MarketInfo(Symbol(), MODE_DIGITS)),
									NormalizeDouble(buyTakeProfit, MarketInfo(Symbol(), MODE_DIGITS)), 0);
						}
					} 
					else if (OrderType() == OP_SELL) {
						if (OrderTakeProfit() != sellTakeProfit) {
							OrderModify(OrderTicket(),
									NormalizeDouble(OrderOpenPrice(), MarketInfo(Symbol(), MODE_DIGITS)),
									NormalizeDouble(OrderStopLoss(), MarketInfo(Symbol(), MODE_DIGITS)),
									NormalizeDouble(sellTakeProfit, MarketInfo(Symbol(), MODE_DIGITS)), 0);
						}
					}
				}
			}
		}
		
		if (buyOrders == 0 && signal == 1 || buyOrders > 0
				&& buyOpenPrice - MarketInfo(Symbol(), MODE_ASK) > buyNextStep * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier) {
			
			RefreshRates();
			OrderSend(
					Symbol(),
					OP_BUY,
					NormalizeDouble(
							MathMax(0.01, AccountBalance() / BASE_LOT_EACH
									* BASE_LOT_SIZE * buyNextUnits), 2), MarketInfo(Symbol(), MODE_ASK),
					MAX_SLIPPAGE_PIPS * pointMultiplier, 0, MarketInfo(Symbol(), MODE_ASK)
							+ TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, ""
							+ buyNextIndex, MAGIC_NUMBER);
		}
		
		if (sellOrders == 0 && signal == (-1) || sellOrders > 0
				&& MarketInfo(Symbol(), MODE_BID) - sellOpenPrice > sellNextStep * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier) {
			
			RefreshRates();
			OrderSend(
					Symbol(),
					OP_SELL,
					NormalizeDouble(
							MathMax(0.01, AccountBalance() / BASE_LOT_EACH
									* BASE_LOT_SIZE * sellNextUnits), 2), MarketInfo(Symbol(), MODE_BID),
					MAX_SLIPPAGE_PIPS * pointMultiplier, 0, MarketInfo(Symbol(), MODE_BID)
							- TAKE_PROFIT_PIPS * MarketInfo(Symbol(), MODE_POINT) * pointMultiplier, ""
							+ sellNextIndex, MAGIC_NUMBER);
		}
		
		return (0);
	}
}
