package controllers;

import java.util.HashMap;

/**
 * @author paco@hernandezgomez.com
 */
public class FxcmController extends Controller {
	
	@Override
	public long timeCurrent() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isTradeAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isTradeContextBusy() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String symbol() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int iBars(String symbol, int timeframe) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double iHigh(String symbol, int timeframe, int shift) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double iLow(String symbol, int timeframe, int shift) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double iOpen(String symbol, int timeframe, int shift) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double iClose(String symbol, int timeframe, int shift) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double iVolume(String symbol, int timeframe, int shift) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean refreshRates() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public double marketInfo(String symbol, int type) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean orderSelect(int number, int selectionType) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean orderSelect(int number, int selectionType, int ordersPool) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public double orderProfit() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double orderSwap() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double orderCommission() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int ordersTotal() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int ordersHistoryTotal() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double accountBalance() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double accountEquity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public long orderOpenTime() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int orderType() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String orderSymbol() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int orderMagicNumber() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int orderTicket() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double orderLots() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double orderClosePrice() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double orderOpenPrice() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double orderStopLoss() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double orderTakeProfit() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean orderClose(int ticket, double lots, double closePrice,
			double maxSlippage) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean orderModify(int ticket, double price, double stopLoss,
			double takeProfit, long expiration) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int orderSend(String symbol, int type, double lots,
			double openPrice, double maxSlippage, double stopLoss,
			double takeProfit, String comment, int magicNumber) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void reset(HashMap<String, Integer> symbolsMap, String[] symbols, double pointMultiplier) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void newBar(int symbol, long time, double open, double low,
			double high, double close, double volume) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String orderComment() {
		// TODO Auto-generated method stub
		return null;
	}
}
