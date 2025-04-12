package org.openjfx;

import java.util.*;
import java.util.Map.Entry;
class Pair<T1,T2>{
	 T1 first;
	 T2 second;
	 Pair(T1 a, T2 b){
		 first = a;
		 second = b;
	 }
	 public String toString() {
		return first+" "+second;
	 }
	 public int hashcode() {
		 int hash1 = first.hashCode();
	     int hash2 = second.hashCode();
	     return hash1 ^ (hash2 + 0x9e3779b9 + (hash1 << 6) + (hash1 >> 2));
	 }
}
// Y , X
class reachable{
	ArrayList<Pair<pieces,Pair<Integer,Integer>>> reach = new ArrayList<Pair<pieces,Pair<Integer,Integer>>>(); // what piece can reach here
	ArrayList<Pair<pieces,Pair<Integer,Integer>>> unactive_reach = new ArrayList<Pair<pieces,Pair<Integer,Integer>>>(); // who all can reach but need a piece there
	ArrayList<Pair<pieces,Pair<Integer,Integer>>> secondary_reach = new ArrayList<Pair<pieces,Pair<Integer,Integer>>>(); // this is my solution to pin states 
}
class pieces{
	int id=0;
	int team=0;
	int coords=0;
	ArrayList<Pair<Integer,Integer>> moves= new ArrayList<Pair<Integer,Integer>>(), attacks = new ArrayList<Pair<Integer,Integer>>();
	HashSet<Pair<Integer,Integer>> legals = new HashSet<Pair<Integer,Integer>>();
	pieces(int i,int t,int coords){
		id = i;
		team = t;
		this.coords = coords;
	}
	boolean checker(HashSet<Pair<Integer,Integer>> e, Pair<Integer,Integer> p, int k ) {
		Iterator<Pair<Integer, Integer>> i = e.iterator(); // agar diagonal ya sidha sidha me koi banda ajaye then u dont move forward
		if (id==3) {
			return true;
		}
		while(i.hasNext()) {
			Pair<Integer,Integer> bruh = (Pair<Integer,Integer>)i.next();
			if (p.first==0 || p.second==0) {
				if (bruh.first==0 && p.first ==0) {
					if (p.second==bruh.second*(Math.abs(p.second))) {
						return false;
					}
				}
				else if (bruh.second==0 && p.second==0) {
					if (p.first==bruh.first*(Math.abs(p.first))) {
						return false;
					}
				}
			}
			else if(bruh.first!=0 && bruh.second!=0) {
				if ( p.second==bruh.second*(Math.abs(p.second)) && p.first==bruh.first*(Math.abs(p.first))) {
					return false;
				}
			}
		}
		return true;
	}
	void isLegal(board_array m) {
		legals.clear();
		HashSet<Pair<Integer,Integer>> temp = new HashSet<Pair<Integer,Integer>>();
		HashSet<Pair<Integer,Integer>> temp2 = new HashSet<Pair<Integer,Integer>>();
		HashMap<Integer, Integer> temp3 = new HashMap<Integer,Integer>();
		HashSet<Pair<Integer,Integer>> temp4 = new HashSet<Pair<Integer,Integer>>();
		for(int i=0; i<moves.size();i++) { 
			int y = coords/8;
			int x = coords%8;
			if (((y+moves.get(i).first)<8 && (x+moves.get(i).second)<8) && ((y+moves.get(i).first)>=0 && (x+moves.get(i).second)>=0)){
				if (m.m.get((y+moves.get(i).first)*8 + x+moves.get(i).second).first.id==0 && checker(temp,moves.get(i),id)) {
					if (id==1 && ((moves.get(i).first==-2 && y!=6)||(moves.get(i).first==2 && y!=1))) {
						continue;
					}
					legals.add(new Pair<Integer,Integer>(y+moves.get(i).first,x+moves.get(i).second));
				}
				else {
					temp.add(new Pair<Integer,Integer>(moves.get(i).first/(moves.get(i).first!=0?Math.abs(moves.get(i).first):1), moves.get(i).second/(moves.get(i).second!=0?Math.abs(moves.get(i).second):1)));
				}
			}
		}
		temp.clear();
		for(int i=0; i<attacks.size();i++) {
			int y = coords/8;
			int x = coords%8;
			if (((y+attacks.get(i).first)<8 && (x+attacks.get(i).second)<8) && ((y+attacks.get(i).first)>=0 && (x+attacks.get(i).second)>=0)){
				if (m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).first.id!=0 && m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).first.team!=this.team  && checker(temp,attacks.get(i),id)) {
					legals.add(new Pair<Integer,Integer>(y+attacks.get(i).first,x+attacks.get(i).second));
					m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).second.reach.add(new Pair<pieces,Pair<Integer,Integer>>(this,new Pair<Integer,Integer>(y,x)));
					temp.add(new Pair<Integer,Integer>(attacks.get(i).first/(attacks.get(i).first!=0?Math.abs(attacks.get(i).first):1), attacks.get(i).second/(attacks.get(i).second!=0?Math.abs(attacks.get(i).second):1)));
					temp4.add(new Pair<Integer,Integer>(attacks.get(i).first/(attacks.get(i).first!=0?Math.abs(attacks.get(i).first):1), attacks.get(i).second/(attacks.get(i).second!=0?Math.abs(attacks.get(i).second):1)));
					temp3.put(((attacks.get(i).first/(attacks.get(i).first!=0?Math.abs(attacks.get(i).first):1))*8+ (attacks.get(i).second/(attacks.get(i).second!=0?Math.abs(attacks.get(i).second):1))),(y+attacks.get(i).first)*8 + x+attacks.get(i).second);
				}
				else if(m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).first.id!=0 && checker(temp,attacks.get(i),id)) { //protection
					m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).second.reach.add(new Pair<pieces,Pair<Integer,Integer>>(this,new Pair<Integer,Integer>(y,x)));
					temp.add(new Pair<Integer,Integer>(attacks.get(i).first/(attacks.get(i).first!=0?Math.abs(attacks.get(i).first):1), attacks.get(i).second/(attacks.get(i).second!=0?Math.abs(attacks.get(i).second):1)));
				}
				else if(m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).first.id!=0 && checker(temp2,attacks.get(i),id) &&  !checker(temp4,attacks.get(i),id) ) { //pin
					m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).second.secondary_reach.add(new Pair<pieces,Pair<Integer,Integer>>(m.m.get(temp3.get(((attacks.get(i).first/(attacks.get(i).first!=0?Math.abs(attacks.get(i).first):1))*8+ (attacks.get(i).second/(attacks.get(i).second!=0?Math.abs(attacks.get(i).second):1))))).first,new Pair<Integer,Integer>(y,x)));
					temp2.add(new Pair<Integer,Integer>(attacks.get(i).first/(attacks.get(i).first!=0?Math.abs(attacks.get(i).first):1), attacks.get(i).second/(attacks.get(i).second!=0?Math.abs(attacks.get(i).second):1)));
				}
				else if(m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).first.id==0 && checker(temp,attacks.get(i),id) && id!=1) {
					m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).second.reach.add(new Pair<pieces,Pair<Integer,Integer>>(this,new Pair<Integer,Integer>(y,x)));
				}
				else if(m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).first.id==0 && checker(temp,attacks.get(i),id) && id==1) {
					if (board_array.prev !=null) {
						if (board_array.prev.second.first==(y) && (((x)==(board_array.prev.second.second-1))||((x)==(board_array.prev.second.second+1))) && (board_array.prev.first.first==1 && board_array.prev.first.second!=team) && (x+attacks.get(i).second == board_array.prev.second.second)) {
							legals.add(new Pair<Integer,Integer>(y+attacks.get(i).first,x+attacks.get(i).second));
							m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).second.reach.add(new Pair<pieces,Pair<Integer,Integer>>(this,new Pair<Integer,Integer>(y,x)));
						}
					}
					m.m.get((y+attacks.get(i).first)*8 + x+attacks.get(i).second).second.unactive_reach.add(new Pair<pieces,Pair<Integer,Integer>>(this,new Pair<Integer,Integer>(y,x)));
				}
			}
		}
		
	}
}
class pawn extends pieces{

	pawn(int i, int t,int coords) {
		super(i, t,coords);
		if (t==2) {
			moves.add(new Pair<Integer,Integer>(-1,0));
			moves.add(new Pair<Integer,Integer>(-2,0));
			attacks.add(new Pair<Integer,Integer>(-1,1));
			attacks.add(new Pair<Integer,Integer>(-1,-1));
		}
		else {
			moves.add(new Pair<Integer,Integer>(1,0));
			moves.add(new Pair<Integer,Integer>(2,0));
			attacks.add(new Pair<Integer,Integer>(1,1));
			attacks.add(new Pair<Integer,Integer>(1,-1));
		}
	}
	
}
class knight extends pieces{

	knight(int i, int t,int coords) {
		super(i, t,coords);
		if (t==2) {
			moves.add(new Pair<Integer,Integer>(-2,1));
			moves.add(new Pair<Integer,Integer>(-2,-1));
			moves.add(new Pair<Integer,Integer>(2,-1));
			moves.add(new Pair<Integer,Integer>(2,1));
			moves.add(new Pair<Integer,Integer>(1,2));
			moves.add(new Pair<Integer,Integer>(-1,2));
			moves.add(new Pair<Integer,Integer>(-1,-2));
			moves.add(new Pair<Integer,Integer>(1,-2));
		}
		else {
			moves.add(new Pair<Integer,Integer>(2,1));
			moves.add(new Pair<Integer,Integer>(2,-1));
			moves.add(new Pair<Integer,Integer>(-2,-1));
			moves.add(new Pair<Integer,Integer>(-2,1));
			moves.add(new Pair<Integer,Integer>(-1,2));
			moves.add(new Pair<Integer,Integer>(1,2));
			moves.add(new Pair<Integer,Integer>(1,-2));
			moves.add(new Pair<Integer,Integer>(-1,-2));
		}
		attacks=moves;
	}
	
}
class bishop extends pieces{

	bishop(int i, int t,int coords) {
		super(i, t,coords);
		for(int j=1; j<8;j++) {
			moves.add(new Pair<Integer,Integer>(j,j));
			moves.add(new Pair<Integer,Integer>(-j,j));
			moves.add(new Pair<Integer,Integer>(j,-j));
			moves.add(new Pair<Integer,Integer>(-j,-j));
		}
		attacks=moves;
	}
	
}
class rook extends pieces{
	rook(int i, int t,int coords) {
		super(i, t,coords);
		for(int j=1; j<8;j++) {
			moves.add(new Pair<Integer,Integer>(0,j));
			moves.add(new Pair<Integer,Integer>(-j,0));
			moves.add(new Pair<Integer,Integer>(0,-j));
			moves.add(new Pair<Integer,Integer>(j,0));
		}
		attacks=moves;
	}
	
}
class queen extends pieces{

	queen(int i, int t,int coords) {
		super(i, t,coords);
		for(int j=1; j<8;j++) {
			moves.add(new Pair<Integer,Integer>(0,j));
			moves.add(new Pair<Integer,Integer>(-j,0));
			moves.add(new Pair<Integer,Integer>(0,-j));
			moves.add(new Pair<Integer,Integer>(j,0));
			moves.add(new Pair<Integer,Integer>(j,j));
			moves.add(new Pair<Integer,Integer>(-j,j));
			moves.add(new Pair<Integer,Integer>(j,-j));
			moves.add(new Pair<Integer,Integer>(-j,-j));
		}
		attacks=moves;
	}
	
}
class king extends pieces{

	king(int i, int t,int coords) {
		super(i, t,coords);
		moves.add(new Pair<Integer,Integer>(0,1));
		moves.add(new Pair<Integer,Integer>(-1,0));
		moves.add(new Pair<Integer,Integer>(0,-1));
		moves.add(new Pair<Integer,Integer>(1,0));
		moves.add(new Pair<Integer,Integer>(1,1));
		moves.add(new Pair<Integer,Integer>(-1,1));		moves.add(new Pair<Integer,Integer>(1,-1));
		moves.add(new Pair<Integer,Integer>(-1,-1));
		attacks=moves;
	}
}
class board_array{
	HashMap<Integer,Pair<pieces,reachable>> m = new HashMap<Integer,Pair<pieces,reachable>>(8);
	Integer king_w,king_b;
	Boolean check=false;
	int turn=1,result=-1;
	static Pair<Pair<Integer,Integer>,Pair<Integer,Integer>> prev = null;
	int movekw=0,movekb=0,moverw1=0,moverw2=0,moverb1=0,moverb2=0;
	pieces rookw2,rookb2;
 	board_array(){
 		prev = null;
		for(int i=2; i<6;i++) {
			for(int j=0; j<8;j++) {
				m.put(i*8+j,new Pair<pieces, reachable>(new pieces(0,0,i*8+j),new reachable()));
			}
		}
		int bruh = 43265234;
		for(int i=0; i<8;i++) {
			m.put(8+i,new Pair<pieces, reachable>(new pawn(1,1,8+i),new reachable()));
			m.put(48+i,new Pair<pieces, reachable>(new pawn(1,2,48+i),new reachable()));
			pieces pp = null,pp2=null;
			int county=0;
			switch (bruh%10) {
				case 3:
					pp=new knight(bruh%10,1,i);
					pp2 = new knight(bruh%10,2,56+i);
					break;
				case 2:
					pp=new bishop(bruh%10,1,i);
					pp2 = new bishop(bruh%10,2,56+i);
					break;
				case 4:
					pp=new rook(bruh%10,1,i);
					pp2 = new rook(bruh%10,2,56+i);
					if (county==0) {
						rookw2=pp;
						rookb2=pp2;
						county=1;
					}
					break;
				case 5:
					pp=new queen(bruh%10,1,i);
					pp2 = new queen(bruh%10,2,56+i);
					break;
				case 6:
					pp=new king(bruh%10,1,i);
					pp2 = new king(bruh%10,2,56+i);
					break;
			}
			m.put(i,new Pair<pieces, reachable>(pp,new reachable()));
			m.put(56+i,new Pair<pieces, reachable>(pp2,new reachable()));
			king_w = 4;
			king_b = 60;
			bruh/=10;
		}
		Legality();
	}
 	void Legality() {
 		for (Entry<Integer, Pair<pieces, reachable>> e : m.entrySet()) {
 			e.getValue().second.reach.clear();
 			e.getValue().second.unactive_reach.clear();
 			e.getValue().second.secondary_reach.clear();
 		}
 		for (Entry<Integer, Pair<pieces, reachable>> e : m.entrySet()) {
 			if (e.getValue().first.id!=0) {
 				e.getValue().first.isLegal(this);
 				if (e.getValue().first.id==6) {
 					if (e.getValue().first.team==1) {
 						king_w = e.getValue().first.coords;
 					}
 					else {
 						king_b = e.getValue().first.coords;
 					}
 				}
 			}
 		}
 		ArrayList<Pair<Integer,Integer>> tmp = new ArrayList<Pair<Integer,Integer>>();
		for(Pair<Integer, Integer> f : m.get(king_w).first.legals) {
			for(Pair<pieces, Pair<Integer, Integer>> g:m.get(f.first*8+f.second).second.reach) {
				if (g.first.team == 2) {
					tmp.add(f);
				}
			}
			for(Pair<pieces, Pair<Integer, Integer>> g:m.get(f.first*8+f.second).second.unactive_reach) {
				if (g.first.team == 2) {
					tmp.add(f);
				}
			}
		}
		m.get(king_w).first.legals.removeAll(tmp);
		tmp.clear();
		for(Pair<Integer, Integer> f : m.get(king_b).first.legals) {
			for(Pair<pieces, Pair<Integer, Integer>> g:m.get(f.first*8+f.second).second.reach) {
				if (g.first.team == 1) {
					tmp.add(f);
				}
			}
			for(Pair<pieces, Pair<Integer, Integer>> g:m.get(f.first*8+f.second).second.unactive_reach) {
				if (g.first.team == 1) {
					tmp.add(f);
				}
			}
		}
		m.get(king_b).first.legals.removeAll(tmp);
		tmp.clear();
		int run=0,block=0,attack=0;
		int checkw=0,checkb=0;
		if (m.get(king_w).second.reach.size()!=0) {
			int count=0;
			ArrayList<Pair<Integer,Pair<Integer,Integer>>> orientations = new ArrayList<Pair<Integer,Pair<Integer,Integer>>>();
			Pair<pieces,Pair<Integer,Integer>> bruh = null;
			for(Pair<pieces, Pair<Integer, Integer>> e : m.get(king_w).second.reach) {
				if (e.first.team!=1) {
					orientations.add(new Pair<Integer,Pair<Integer,Integer>>(e.first.id,e.second));
					count++;
					bruh=e;
				}
			}
			for(Pair<Integer, Integer> really: m.get(king_w).first.legals) {
				for(int jj=0;jj<orientations.size();jj++) {
					if ((really.first==orientations.get(jj).second.first || really.second==orientations.get(jj).second.second)&& (orientations.get(jj).first==4 && orientations.get(jj).first==5)) {
						tmp.add(really);
					}
					else if ((Math.abs(really.first-orientations.get(jj).second.first)==Math.abs(really.second-orientations.get(jj).second.second))&& (orientations.get(jj).first!=4 && orientations.get(jj).first!=3)) {
						tmp.add(really);
					}
				}
			}
			m.get(king_w).first.legals.removeAll(tmp);
			tmp.clear();
			if (count>=1) {
			    checkw=1;
				ArrayList<Pair<pieces,Pair<Integer,Integer>>> pawnw = new ArrayList<Pair<pieces,Pair<Integer,Integer>>>();
				for(Entry<Integer, Pair<pieces, reachable>> omg : m.entrySet()) {
					if (omg.getValue().first.team==1 && omg.getValue().first.id!=6) {
						if (omg.getValue().first.id==1) {
							for(Pair<Integer,Integer> ree: omg.getValue().first.legals) {
								pawnw.add(new Pair<pieces,Pair<Integer,Integer>>(omg.getValue().first,ree));
							}
						}
						omg.getValue().first.legals.clear();
					}
				}
				if (m.get(king_w).first.legals.size()==0) {
					run=1;
				}
				if (count>1) {
					block=1;attack=1;
					
				}
				else {
					attack=1; block=1;
					int checkery=1;
					for (Pair<pieces,Pair<Integer,Integer>> pp: m.get(bruh.second.first*8+bruh.second.second).second.reach) {
						if (pp.first.team!=2 && pp.first.id!=6) {
							pp.first.legals.add(bruh.second);
							attack=0;
						}
						if (pp.first.team==2) {
							checkery=0;
						}
					}
					if (checkery==1) {
						for (Pair<pieces,Pair<Integer,Integer>> pp: m.get(bruh.second.first*8+bruh.second.second).second.reach) {
							if (pp.first.team!=2 && pp.first.id==6) {
								pp.first.legals.add(bruh.second);
								attack=0;
							}
						}
					}
					int xx = king_w%8;
					int yy = king_w/8;
					int tx = (bruh.second.second-xx)==0?0:Math.abs(bruh.second.second-xx)/(bruh.second.second-xx);
					int ty = (bruh.second.first-yy)==0?0:Math.abs(bruh.second.first-yy)/(bruh.second.first-yy);
					if (bruh.first.id!=3) {
						xx+=tx;
						yy+=ty;
						while((xx!=bruh.second.second || yy!=bruh.second.first) && xx<8 && yy<8) {
					    	for(Pair<pieces,Pair<Integer,Integer>> ppp : m.get(yy*8+xx).second.reach) {
					    		if (ppp.first.team==1 && ppp.first.id!=6) {
						    		ppp.first.legals.add(new Pair<Integer,Integer>(yy,xx));
						    		block=0;
					    		}
					    	}
					    	for(Pair<pieces, Pair<Integer, Integer>> ppp: pawnw) {
					    		if (ppp.second.second==xx && ppp.second.first==yy) {
					    			ppp.first.legals.add(ppp.second);
						    		block=0;
						    	}
					    	}
					    	xx+=tx;
					    	yy+=ty;
					    }
					    
					}
				}
				if (block==1 && attack==1 && run==1) {
					result=2; // black wins
				}
			}
		}
		if (m.get(king_b).second.reach.size()!=0) {
			int count=0;
			ArrayList<Pair<Integer,Pair<Integer,Integer>>> orientations = new ArrayList<Pair<Integer,Pair<Integer,Integer>>>();
			Pair<pieces,Pair<Integer,Integer>> bruh = null;
			for(Pair<pieces, Pair<Integer, Integer>> e : m.get(king_b).second.reach) {
				if (e.first.team!=2) {
					orientations.add(new Pair<Integer,Pair<Integer,Integer>>(e.first.id,e.second));
					count++;
					bruh=e;
				}
			}
			for(Pair<Integer, Integer> really: m.get(king_b).first.legals) {
				for(int jj=0;jj<orientations.size();jj++) {
					if ((really.first==orientations.get(jj).second.first || really.second==orientations.get(jj).second.second)&& (orientations.get(jj).first==4 && orientations.get(jj).first==5)) {
						tmp.add(really);
					}
					else if ((Math.abs(really.first-orientations.get(jj).second.first)==Math.abs(really.second-orientations.get(jj).second.second))&& (orientations.get(jj).first!=4 && orientations.get(jj).first!=3)) {
						tmp.add(really);
					}
				}
			}
			m.get(king_b).first.legals.removeAll(tmp);
			tmp.clear();
			if (count>=1) {
			    checkb=1;
				ArrayList<Pair<pieces,Pair<Integer,Integer>>> pawnw = new ArrayList<Pair<pieces,Pair<Integer,Integer>>>();
				for(Entry<Integer, Pair<pieces, reachable>> omg : m.entrySet()) {
					if (omg.getValue().first.team==2 && omg.getValue().first.id!=6) {
						if (omg.getValue().first.id==1) {
							for(Pair<Integer,Integer> ree: omg.getValue().first.legals) {
								pawnw.add(new Pair<pieces,Pair<Integer,Integer>>(omg.getValue().first,ree));
							}
						}
						omg.getValue().first.legals.clear();
					}
				}
				if (m.get(king_b).first.legals.size()==0) {
					run=1;
				}
				if (count>1) {
					block=1;attack=1;
					
				}
				else {
					attack=1; block=1;
					int checker=1;
					for (Pair<pieces,Pair<Integer,Integer>> pp: m.get(bruh.second.first*8+bruh.second.second).second.reach) {
						if (pp.first.team!=1 && pp.first.id!=6) {
							pp.first.legals.add(bruh.second);
							attack=0;
						}
						else if(pp.first.team==1) {
							checker=0;
						}
					}
					if (checker==1) {
						for (Pair<pieces,Pair<Integer,Integer>> pp: m.get(bruh.second.first*8+bruh.second.second).second.reach) {
							if (pp.first.team!=1 && pp.first.id==6) {
								pp.first.legals.add(bruh.second);
								attack=0;
							}
						}
					}
					int xx = king_b%8;
					int yy = king_b/8;
					int tx = (bruh.second.second-xx)==0?0:Math.abs(bruh.second.second-xx)/(bruh.second.second-xx);
					int ty = (bruh.second.first-yy)==0?0:Math.abs(bruh.second.first-yy)/(bruh.second.first-yy);
					if (bruh.first.id!=3) {
						xx+=tx;
						yy+=ty;
						while((xx!=bruh.second.second || yy!=bruh.second.first) && xx<8 && yy<8) {
					    	for(Pair<pieces,Pair<Integer,Integer>> ppp : m.get(yy*8+xx).second.reach) {
					    		if (ppp.first.team==2 && ppp.first.id!=6) {
						    		ppp.first.legals.add(new Pair<Integer,Integer>(yy,xx));
						    		block=0;
					    		}
					    	}
					    	for(Pair<pieces, Pair<Integer, Integer>> ppp: pawnw) {
					    		if (ppp.second.second==xx && ppp.second.first==yy) {
					    			ppp.first.legals.add(ppp.second);
						    		block=0;
						    	}
					    	}
					    	xx+=tx;
					    	yy+=ty;
					    }
					    
					}
				}
				if (block==1 && attack==1 && run==1) {
					result=1; //white wins
				}
			}
		}
		if (m.get(king_w).second.secondary_reach.size()!=0) {
			for(Pair<pieces, Pair<Integer, Integer>> i : m.get(king_w).second.secondary_reach) {
				tmp.clear();
				for(Pair<Integer, Integer> j : i.first.legals) {
					if (((j.first*((king_w%8)-i.second.second))-(j.second*((king_w/8)-i.second.first)))!=((i.second.first*((king_w%8)-i.second.second))-(i.second.second*((king_w/8)-i.second.first)))) {
						tmp.add(j);
					}
				}
				i.first.legals.removeAll(tmp);
			}
		}
		if (m.get(king_b).second.secondary_reach.size()!=0) {
			for(Pair<pieces, Pair<Integer, Integer>> i : m.get(king_b).second.secondary_reach) {
				tmp.clear();
				for(Pair<Integer, Integer> j : i.first.legals) {
					if (((j.first*((king_b%8)-i.second.second))-(j.second*((king_b/8)-i.second.first)))!=((i.second.first*((king_b%8)-i.second.second))-(i.second.second*((king_b/8)-i.second.first)))) {
						tmp.add(j);
					}
				}
				i.first.legals.removeAll(tmp);
			}
		}
		if (checkw==0 && movekw==0) {
			if (moverw1==0) {   // long castle
				int x = king_w%8-1;
				int y = king_w/8;
				int isAttacked=0;
				int isBlocked=0;
				Master: while(x!=1) {
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.reach) {
						if (i.first.team!=1) {
							isAttacked=1;
							break Master;
						}
					}
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.unactive_reach) {
						if (i.first.team!=1) {
							isAttacked=1;
							break Master;
						}
					}
					if (m.get(y*8+x).first.id!=0) {
						isBlocked=1;
					}
					x--;
				}
				if (isAttacked==0 && isBlocked==0) {
					m.get(king_w).first.legals.add(new Pair<Integer,Integer>(y,king_w%8-2));
				}
			}
			if (moverw2==0) {  //short castle
				int x = king_w%8+1;
				int y = king_w/8;
				int isAttacked=0;
				int isBlocked=0;
				Master: while(x!=7) {
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.reach) {
						if (i.first.team!=1) {
							isAttacked=1;
							break Master;
						}
					}
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.unactive_reach) {
						if (i.first.team!=1) {
							isAttacked=1;
							break Master;
						}
					}
					if (m.get(y*8+x).first.id!=0) {
						isBlocked=1;
					}
					x++;
				}
				if (isAttacked==0 && isBlocked==0) {
					m.get(king_w).first.legals.add(new Pair<Integer,Integer>(y,king_w%8+2));
				}
			}
		}
		if (checkb==0 && movekb==0) {
			if (moverb1==0) {   // long castle
				int x = king_b%8-1;
				int y = king_b/8;
				int isAttacked=0;
				int isBlocked=0;
				Master: while(x!=1) {
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.reach) {
						if (i.first.team!=2) {
							isAttacked=1;
							break Master;
						}
					}
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.unactive_reach) {
						if (i.first.team!=2) {
							isAttacked=1;
							break Master;
						}
					}
					if (m.get(y*8+x).first.id!=0) {
						isBlocked=1;
					}
					x--;
				}
				if (isAttacked==0 && isBlocked==0) {
					m.get(king_b).first.legals.add(new Pair<Integer,Integer>(y,king_b%8-2));
				}
			}
			if (moverb2==0) {  //short castle
				int x = king_b%8+1;
				int y = king_b/8;
				int isAttacked=0;
				int isBlocked=0;
				Master: while(x!=7) {
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.reach) {
						if (i.first.team!=2) {
							isAttacked=1;
							break Master;
						}
					}
					for(Pair<pieces, Pair<Integer, Integer>> i : m.get(y*8+x).second.unactive_reach) {
						if (i.first.team!=2) {
							isAttacked=1;
							break Master;
						}
					}
					if (m.get(y*8+x).first.id!=0) {
						isBlocked=1;
					}
					x++;
				}
				if (isAttacked==0 && isBlocked==0) {
					m.get(king_b).first.legals.add(new Pair<Integer,Integer>(y,king_b%8+2));
				}
			}
		}
		int countw=0,countb=0;
		for (Entry<Integer, Pair<pieces, reachable>> e : m.entrySet()) {
 			if (e.getValue().first.id!=0) {
 				if (e.getValue().first.team==1) {
 					countw+=e.getValue().first.legals.size();
 				}
 				if (e.getValue().first.team==2) {
 					countb+=e.getValue().first.legals.size();
 				}
 			}
 		}
		if ((countw==0 || countb==0) && checkb==0 && checkw==0) {
			result=0;
		}
 	}
 	int move(Pair<Integer,Integer> a, Pair<Integer,Integer> b,boolean c) {
 		int flag=0;
 		for( Pair<Integer,Integer> ee: m.get(a.first*8 + a.second).first.legals) {
 			if (ee.first == b.first && ee.second==b.second) {
 				flag=1;
 				break;
 			}
 		}
 		if (flag==1) {
 			m.get(a.first*8+a.second).first.coords = b.first*8+b.second;
 			if (m.get(a.first*8+a.second).first.id==6) {
 				if (m.get(a.first*8+a.second).first.team==1) {
 					movekw=1;
 				}
 				if (m.get(a.first*8+a.second).first.team==2) {
 					movekb=1;
 				}
 			}
 			if (m.get(a.first*8+a.second).first.id==4) {
 				if (m.get(a.first*8+a.second).first.team==1) {
 					if (m.get(a.first*8+a.second).first==rookw2) {
 						moverw2=1;
 					}
 					else {
 						moverw1=1;
 					}
 				}
 				if (m.get(a.first*8+a.second).first.team==2) {
 					if (m.get(a.first*8+a.second).first==rookb2) {
 						moverb2=1;
 					}
 					else {
 						moverb1=1;
 					}
 				}
 			}
 			if (m.get(a.first*8+a.second).first.id==6 && b.first==a.first && (a.second==b.second-2 || a.second==b.second+2)) {
 				if (a.second==b.second-2) {
 					m.get((b.first*8+(b.second+1))).first.coords = (b.first*8+(b.second-1));
 					m.put((b.first*8+(b.second-1)),m.get((b.first*8+(b.second+1))));
 					m.put((b.first*8+(b.second+1)),new Pair<pieces, reachable>(new pieces(0,0,(b.first*8+(b.second+1))),new reachable()));
 				}
 				if (a.second==b.second+2) {
 					m.get((b.first*8+(b.second-2))).first.coords = (b.first*8+(b.second+1));
 					m.put((b.first*8+(b.second+1)),m.get((b.first*8+(b.second-2))));
 					m.put((b.first*8+(b.second-2)),new Pair<pieces, reachable>(new pieces(0,0,(b.first*8+(b.second-2))),new reachable()));
 				}
 			}
 			if (prev!=null && prev.first.first == 1 && prev.first.second!=m.get(a.first*8+a.second).first.team &&  (b.first-(m.get(a.first*8+a.second).first.team==1?1:-1))==prev.second.first && b.second==prev.second.second && m.get(a.first*8+a.second).first.id==1) {
 				m.put(prev.second.first*8 + prev.second.second, new Pair<pieces, reachable>(new pieces(0,0,prev.second.first*8+prev.second.second),new reachable()));
 			}
 			if(a.first==(1+5*(m.get(a.first*8 + a.second).first.team-1))) {
 				prev = new Pair<Pair<Integer,Integer>, Pair<Integer, Integer>>(new Pair<Integer,Integer>(m.get(a.first*8 + a.second).first.id,m.get(a.first*8 + a.second).first.team),b);
 			}
 			else {
 				prev = null;
 			}
 			if (c){
 				// add a gui for to give options between what he wants..
 				m.put(b.first*8+b.second,new Pair<pieces, reachable>(new queen(5,m.get(a.first*8+a.second).first.team,b.first*8+b.second),new reachable()));
 			}
 			else {
 	 			m.put(b.first*8+b.second,m.get(a.first*8+a.second));
 			}
 			m.put(a.first*8 + a.second, new Pair<pieces, reachable>(new pieces(0,0,a.first*8+a.second),new reachable()));
 			Legality();
 		}
 		return flag;
 	}
}
