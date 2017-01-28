package expertadvisors;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import controllers.Controller;
import com.google.inject.Inject;

/**
 * @author paco@hernandezgomez.com
 */
public abstract class ExpertAdvisor {
	
	protected static Logger log = Logger.getLogger("ExpertAdvisor");	
	
	Controller controller;
	
	@Inject
	public ExpertAdvisor(Controller controller) {
		this.controller = controller;
	}
	
	public static final int MODE_TRADES = 0;
	public static final int MODE_HISTORY = 1;
	
	public static final int SELECT_BY_POS = 0;
	public static final int SELECT_BY_TICKET = 1;
	
	public static final int OP_BUY = 0;
	public static final int OP_SELL = 1;
	public static final int OP_BUYLIMIT = 2;
	public static final int OP_SELLLIMIT = 3;
	public static final int OP_BUYSTOP = 4;
	public static final int OP_SELLSTOP = 5;
	
	public static final int MODE_LOW = 1;
	public static final int MODE_HIGH = 2;
	public static final int MODE_TIME = 5;
	public static final int MODE_BID = 9;
	public static final int MODE_ASK = 10;
	public static final int MODE_POINT = 11;
	public static final int MODE_DIGITS = 12;
	public static final int MODE_SPREAD = 13;
	public static final int MODE_STOPLEVEL = 14;
	public static final int MODE_LOTSIZE = 15;
	public static final int MODE_TICKVALUE = 16;
	public static final int MODE_TICKSIZE = 17;
	public static final int MODE_SWAPLONG = 18;
	public static final int MODE_SWAPSHORT = 19;
	public static final int MODE_STARTING = 20;
	public static final int MODE_EXPIRATION = 21;
	public static final int MODE_TRADEALLOWED = 22;
	public static final int MODE_MINLOT = 23;
	public static final int MODE_LOTSTEP = 24;
	public static final int MODE_MAXLOT = 25;
	public static final int MODE_SWAPTYPE = 26;
	public static final int MODE_PROFITCALCMODE = 27;
	public static final int MODE_MARGINCALCMODE = 28;
	public static final int MODE_MARGININIT = 29;
	public static final int MODE_MARGINMAINTENANCE = 30;
	public static final int MODE_MARGINHEDGED = 31;
	public static final int MODE_MARGINREQUIRED = 32;
	public static final int MODE_FREEZELEVEL = 33;
	
	public static final int PERIOD_M1 = 0;
	public static final int PERIOD_M5 = 1;
	public static final int PERIOD_M15 = 2;
	public static final int PERIOD_M30 = 3;
	public static final int PERIOD_H1 = 4;
	public static final int PERIOD_H4 = 5;
	public static final int PERIOD_D1 = 6;
	public static final int PERIOD_W1 = 7;
	public static final int PERIOD_MN1 = 8;
	
	public static final int TIME_DATE = 1;
	public static final int TIME_SECONDS = 4;
	
	public abstract int init();
	public abstract int deinit();
	public abstract int start();
	
	long TimeCurrent() {
		return controller.timeCurrent();
	}
	
	int TimeMinute(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.MINUTE);
	}
	
	int TimeHour(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	int TimeDay(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.DATE);
	}
	
	int TimeDayOfWeek(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		
		case Calendar.SUNDAY:
			return 0;
			
		case Calendar.MONDAY:
			return 1;
			
		case Calendar.TUESDAY:
			return 2;
			
		case Calendar.WEDNESDAY:
			return 3;
			
		case Calendar.THURSDAY:
			return 4;
			
		case Calendar.FRIDAY:
			return 5;
			
		case Calendar.SATURDAY:
			return 6;
		}
		
		return (-1);
	}
	
	int TimeMonth(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.MONTH);
	}
	
	int TimeYear(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.YEAR);
	}
	
	long TimeLocal() {
		return new Date().getTime();
	}
	
	String TimeToStr(long value, int mode) {
		Date date = new Date(value);
		return date.toString();
	}
	
	Integer StrToInteger(String str) {
		return Integer.parseInt(str);
	}
	
	long StrToTime(String value) {
		return (-1);
	}
	
	void Print(String data) {
		log.info(data);
	}
	
	boolean IsConnected() {
		return controller.isConnected();
	}
	
	boolean IsTradeAllowed() {
		return controller.isTradeAllowed();
	}
	
	boolean IsTradeContextBusy() {
		return controller.isTradeContextBusy();
	}
	
	boolean IsDllsAllowed() {
		return true;
	}
	
	boolean IsExpertEnabled() {
		return true;
	}
	
	boolean IsStopped() {
		return false;
	}
	
	boolean IsTesting() {
		return false;
	}
	
	int ArraySize(Object[] array) {
		return array.length;
	}
	
	String Symbol() {
		return controller.symbol();
	}
	
	int iBars(String symbol, int timeframe) {
		return controller.iBars(symbol, timeframe);
	}
	
	double iHigh(String symbol, int timeframe, int shift) {
		return controller.iHigh(symbol, timeframe, shift);
	}
	
	double iLow(String symbol, int timeframe, int shift) {
		return controller.iLow(symbol, timeframe, shift);
	}
	
	double iOpen(String symbol, int timeframe, int shift) {
		return controller.iOpen(symbol, timeframe, shift);
	}
	
	double iClose(String symbol, int timeframe, int shift) {
		return controller.iClose(symbol, timeframe, shift);
	}
	
	double iVolume(String symbol, int timeframe, int shift) {
		return controller.iVolume(symbol, timeframe, shift);
	}
	
	boolean RefreshRates() {
		return controller.refreshRates();
	}
	
	double NormalizeDouble(double value, int digits) {
		return value;
	}
	
	double NormalizeDouble(double value, double digits) {
		return value;
	}
	
	double MathFloor(double value) {
		return Math.floor(value);
	}
	
	double MathMax(double value1, double value2) {
		return Math.max(value1, value2);
	}
	
	double MathMin(double value1, double value2) {
		return Math.min(value1, value2);
	}
	
	double MathPow(double value1, double value2) {
		return Math.pow(value1, value2);
	}
	
	double MarketInfo(String symbol, int type) {
		return controller.marketInfo(symbol, type);
	}
	
	double OrderProfit() {
		return controller.orderProfit();
	}
	
	double OrderSwap() {
		return controller.orderSwap();
	}
	
	double OrderCommission() {
		return controller.orderCommission();
	}
	
	int OrdersTotal() {
		return controller.ordersTotal();
	}
	
	int OrdersHistoryTotal() {
		return controller.ordersHistoryTotal();
	}
	
	double AccountBalance() {
		return controller.accountBalance();
	}
	
	double AccountEquity() {
		return controller.accountEquity();
	}
	
	boolean OrderSelect(int number, int selectionType) {
		return controller.orderSelect(number, selectionType);
	}
	
	boolean OrderSelect(int number, int selectionType, int ordersPool) {
		return controller.orderSelect(number, selectionType, ordersPool);
	}
	
	long OrderOpenTime() {
		return controller.orderOpenTime();
	}
	
	int OrderType() {
		return controller.orderType();
	}
	
	String OrderSymbol() {
		return controller.orderSymbol();
	}
	
	String OrderComment() {
		return controller.orderComment();
	}
	
	int OrderMagicNumber() {
		return controller.orderMagicNumber();
	}
	
	int OrderTicket() {
		return controller.orderTicket();
	}
	
	double OrderLots() {
		return controller.orderLots();
	}
	
	double OrderClosePrice() {
		return controller.orderClosePrice();
	}
	
	double OrderOpenPrice() {
		return controller.orderOpenPrice();
	}
	
	double OrderStopLoss() {
		return controller.orderStopLoss();
	}
	
	double OrderTakeProfit() {
		return controller.orderTakeProfit();
	}
	
	boolean OrderClose(int ticket, double lots, double closePrice, double maxSlippage) {
		return controller.orderClose(ticket, lots, closePrice, maxSlippage);
	}
	
    boolean OrderModify(int ticket, double price, double stopLoss, double takeProfit, long expiration) {
    	return controller.orderModify(ticket, price, stopLoss, takeProfit, expiration);
    }
	
    int OrderSend(String symbol, int type, double lots, double openPrice, double maxSlippage, double stopLoss, double takeProfit, String comment, int magicNumber) {
    	return controller.orderSend(symbol, type, lots, openPrice, maxSlippage, stopLoss, takeProfit, comment, magicNumber);
	}
    
    String StringSubstr(String text, int start) {
    	return text.substring(start);
    }
    
    String StringSubstr(String text, int start, int length) {
    	return text.substring(start, start + length);
    }
}
