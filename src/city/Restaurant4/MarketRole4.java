package city.Restaurant4;

import Role.Role;
import java.util.*;

import justinetesting.interfaces.Market4;

public class MarketRole4 extends Role implements Market4 {
	String name;
	CookRole4 cook;
	Timer timer= new Timer();
	List<Food> foods;
	List<Order> orders;
	public enum orderState {pending, filling, successful, unsuccessful, done};
	double money= 0;
	double amount= 0;
	CashierRole4 cashier;
	Map<String, Double> cost= new HashMap<String, Double>();

	public MarketRole4(String name) {
		super();
		this.name= name;
		foods= Collections.synchronizedList(new ArrayList<Food>());
		orders= Collections.synchronizedList(new ArrayList<Order>());
		foods.add(new Food("Steak"));
		foods.add(new Food("Chicken"));
		foods.add(new Food("Salad"));
		foods.add(new Food("Pizza"));
		cost.put("Steak", 10.00);
		cost.put("Chicken", 7.00);
		cost.put("Salad", 3.00);
		cost.put("Pizza", 5.00);
	}

	public String getName(){
		return name;
	}
	
	public void setCashier(CashierRole4 c){
		cashier= c;
	}
	
	
	// MESSAGES 
	public void msgHereIsOrder(CookRole4 c, int st, int ch, int s, int p){
		cook= c;
		orders.add(new Order(c, st, ch, s, p));
		stateChanged();
	}
	
	public void msgHereIsMoney(double amount){
		print("Received money for the delivery, business is going great!");
		money += amount;
		amount= 0;
		stateChanged();
	}
	

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(orders != null){
			synchronized(orders){
				for(Order order : orders){
					if(order.s == orderState.pending){
						print("I should fill this order and get it back to the restaurant right away.");
						processOrder(order);
						return true;
					}
					if(order.s == orderState.done){
						print("Time to send the bill to the cashier!");
						sendBill(order);
						return true;
					}
				}
			}
		}
		synchronized(foods){
			for(Food food : foods){
				if(food.getAmount() == 0){
					foods.remove(food);
					outOfFood(food.type);
					return true;
				}
			}
		}
		return false;
	}

	
	// ACTIONS
	private void processOrder(Order o){
		o.s= orderState.successful;
		for(Food food : foods){
			if(food.getAmount() == 0){
				o.s= orderState.unsuccessful;
			}
			else {
				while(food.getAmount() > 0 && o.foodAmount.get(food.type) > 0){
					food.decrementAmount();	
					o.fillRequest(food.type);
				}
				if(o.foodAmount.get(food.type) != 0){
					o.s= orderState.unsuccessful;
				}
			}
		}
		tellCook(o);
		stateChanged();
	}

	public void tellCook(final Order o){
		timer.schedule(new TimerTask() {
			@Override public void run() { 
				if(o.s == orderState.successful){
					print("Successsful filling of delivery!");
					cook.msgHereIsDelivery(o.st, o.ch, o.sal, o.p, true);
					amount += o.st * cost.get("Steak");
					amount += o.ch * cost.get("Chicken");
					amount += o.sal * cost.get("Salad");
					amount += o.p * cost.get("Pizza");
				}	
				else if(o.s == orderState.unsuccessful){
					print("Oh no, I could't complete the order, here is what I have");
					cook.msgHereIsDelivery(o.st - o.foodAmount.get("Steak"), o.ch-  o.foodAmount.get("Chicken"), o.sal - o.foodAmount.get("Salad"), o.p - o.foodAmount.get("Pizza"), false);
					amount += (o.st - o.foodAmount.get("Steak")) * cost.get("Steak");
					amount += (o.ch - o.foodAmount.get("Chicken")) * cost.get("Chicken");
					amount += (o.sal - o.foodAmount.get("Salad")) * cost.get("Salad");
					amount += (o.p - o.foodAmount.get("Pizza")) * cost.get("Pizza");
				}
				o.s= orderState.done;
				stateChanged();
			}}, 7000);	
	}
	
	public void outOfFood(String type){
		cook.msgOutOfItem(this, type);
	}
	
	public void sendBill(Order o){
		cashier.msgHereIsBill(this, amount);
		orders.remove(o);
		stateChanged();
	}
	
	
	// CLASSES
	public static class Order{
		CookRole4 c;
		Map<String, Integer> foodAmount= new HashMap<String, Integer>();
		int st;
		int ch;
		int sal;
		int p;
		orderState s;
		
		Order(CookRole4 c, int st, int ch, int sal, int p){
			this.c= c;
			foodAmount.put("Steak", st);
			this.st= st;
			foodAmount.put("Chicken", ch);
			this.ch= ch;
			foodAmount.put("Salad", sal);
			this.sal= sal;
			foodAmount.put("Pizza", p);
			this.p= p;
			this.s= orderState.pending;
		}
		
		public void fillRequest(String type){
			Integer temp= foodAmount.get(type);
			foodAmount.put(type, --temp);
		}
	}
	
	public class Food{
		String type;
		private int currAmount;
			
		Food(String type){
			this.type= type;
			currAmount= 5;
		}
		
		public int getAmount(){
			return currAmount;
		}
		
		public void decrementAmount(){
			currAmount--;
		}
	}
}
