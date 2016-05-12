package TankGame3;
import java.util.*;
import java.io.*;

import javax.swing.ImageIcon;
public class Members{
	
}
class Node{
	int x;
	int y;
	int direct;
	Node(int x,int y,int d){
		this.x=x;
		this.y=y;
		this.direct=d;
	}
}

class Recorder
{
	//敌人数量
	private static int enNum=20;
	//自己生命
	private static int myLife=3;
	
	//记录总共消灭的敌人数量
	private static int allenNum=0;
	
	//从文件中恢复记录点;
	static Vector<Node> nodes =new Vector<Node>();
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static FileReader fr=null;
	private static BufferedReader br=null;
	private static Vector<Enemies> ets=new Vector<Enemies>();
	
	//完成读取任务
	public void getNodes(){
		try {
			fr=new FileReader("d:\\tankgame.txt");
			br=new BufferedReader(fr);
			String n="";
			//先读第一行
			n=br.readLine();
			allenNum=Integer.parseInt(n);
			while((n=br.readLine())!=null){ 
				String[] s=n.split(" ");
				
				Node node=new Node(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]));
				
				nodes.add(node);
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			};
			
		}
	}
	
	public static Vector<Enemies> getEts() {
		return ets;
	}

	public  void setEts(Vector<Enemies> ets) {
		this.ets = ets;
	}

	//保存击毁的敌人数量和坐标和方向
	public  void keepRecAndEnemyTank(){
		try {
			//创建文件流
			fw=new FileWriter("d:\\tankgame.txt");
			bw=new BufferedWriter(fw);
			bw.write(allenNum+"\r\n");
			//保存当前活着的敌人tank的坐标和方向；
			for(int i=0;i<ets.size();i++){
				Enemies et=ets.get(i);
				if(et.isLive){
					String record=et.x+" "+et.y+" "+et.direct;
					
					bw.write(record+"\r\n");
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			try {
				//后开先关；
				bw.flush();
				fw.flush();
				bw.close();
				fw.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	//把玩家击毁敌人坦克数量保存到文件中
	public static void keepRecording()
	{
		try {
			//创建文件流
			fw=new FileWriter("d:\\tankgame.txt");
			bw=new BufferedWriter(fw);
			bw.write(allenNum+"\r\n");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			try {
				//后开先关；
				bw.flush();
				fw.flush();
				bw.close();
				fw.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	//从文件中读取数据
	public static void getRecording(){
		try {
			fr=new FileReader("d:\\tankgame.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			allenNum=Integer.parseInt(n);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			};
			
		}
		
	}
	
	
	
	public static int getAllenNum() {
		return allenNum;
	}
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	
	//减少敌人数；
	public static void reduceEnNum(){
		enNum--;
	}
	
	//自己生命减少
	public static void reduceMylife(){
		myLife--;
	}
	
	public static void addEnNum(){
		allenNum+=10;
	}
	
}



class Tank {
	int x=0;	//表示坦克的横纵坐标
	int y=0;
	int direct=0;	//方向，0表示上，1表示右，2下，3左
	//坦克的速度
	int speed=3;
	boolean isLive=true;
	public Tank(int x,int y){
		this.x=x;
		this.y=y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}

//自己的坦克类
class Hero extends Tank{
	
	//Bullet bullet=null;
	Vector<Bullet> bu=new Vector<Bullet>(); 
	Bullet bullet=null;
	public Hero(int x,int y){
		super(x,y);	
	}
	//开火
	public void shot(){
		
		switch(this.direct){
			case 0:
				//创建一颗子弹;
				bullet=new Bullet();
				bullet.setX(this.x+8);
				bullet.setY(this.y-6);
				bullet.setDirect(0);
				//把子弹加入到向量;
				bu.add(bullet);
				break;
			case 1:
				bullet=new Bullet();
				bullet.setX(this.x+36);
				bullet.setY(this.y+8);
				bullet.setDirect(1);
				bu.add(bullet);
				break;
			case 2:
				bullet=new Bullet();
				bullet.setX(this.x+8);
				bullet.setY(this.y+36);
				bullet.setDirect(2);
				bu.add(bullet);
				break;
			case 3:
				bullet=new Bullet();
				bullet.setX(this.x-7);
				bullet.setY(this.y+8);
				bullet.setDirect(3);
				bu.add(bullet);
				break;
		}
		Thread t=new Thread(bullet);
		t.start();
		
	}
	
	//坦克移动
	public void moveUp(){
		this.y-=speed;
	}
	public void moveRight(){
		this.x+=speed;
	}
	public void moveDown(){
		this.y+=speed;
	}
	public void moveLeft(){
		this.x-=speed;
	}
}


//敌人坦克
class Enemies extends Tank implements Runnable{
	
	Vector<Enemies> en=new Vector<Enemies>();
	//定义一个向量，存放子弹
	Vector<Bullet> enemyBu=new Vector<Bullet>();
	int times=0;
	public Enemies(int x,int y){
		super(x,y);
		direct=2;
	}
	
	public void setEnemy(Vector<Enemies> vv){
		this.en=vv;
	}
	
	//判断是否碰到了敌人坦克;
	public boolean isTouchTank(){
		boolean b=false;
		switch(this.direct){
			case 0:
				//自己的坦克向上，取出所有的敌人坦克；
				for(int i=0;i<en.size();i++){
					Enemies et=en.get(i);	
					if(et!=this){	//取出的不是自己时
						if(et.direct==0||et.direct==2){
							//如果敌人方向向上或向下
							if(this.x>=et.x&&this.x<=et.x+20&&this.y>et.y&&this.y<=et.y+30){
								return true;
							}
							if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
								return true;
							}
						}
						if(et.direct==1||et.direct==3){
							//如果敌人坦克往左或右
							if(this.x>=et.x&&this.x<=et.x+30&&this.y>et.y&&this.y<=et.y+20){
								return true;
							}
							if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20){
								return true;
							}

						}
					}
				}
				
				break;
			case 1:
				//坦克向右
				for(int i=0;i<en.size();i++){
					Enemies et=en.get(i);	
					if(et!=this){	//取出的不是自己时
						if(et.direct==0||et.direct==2){
							//如果敌人方向向上或向下
							if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y>et.y&&this.y<=et.y+30){
								return true;
							}
							if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
								return true;
							}
						}
						if(et.direct==1||et.direct==3){
							//如果敌人坦克往左或右
							if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y+20>et.y&&this.y+20<=et.y+30){
								return true;
							}
							if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20){
								return true;
							}

						}
					}
				}
				break;
			case 2:
				//坦克向下
				for(int i=0;i<en.size();i++){
					Enemies et=en.get(i);	
					if(et!=this){	//取出的不是自己时
						if(et.direct==0||et.direct==2){
							//如果敌人方向向上或向下
							if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>et.y&&this.y+30<=et.y+30){
								return true;
							}
							if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30){
								return true;
							}
						}
						if(et.direct==1||et.direct==3){
							//如果敌人坦克往左或右
							if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y+30>et.y&&this.y+30<=et.y+30){
								return true;
							}
							if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20){
								return true;
							}

						}
					}
				}
				break;
			case 3:
				//自己向左
				for(int i=0;i<en.size();i++){
					Enemies et=en.get(i);	
					if(et!=this){	//取出的不是自己时
						if(et.direct==0||et.direct==2){
							//如果敌人方向向上或向下
							if(this.x>=et.x&&this.x<=et.x+20&&this.y>et.y&&this.y<=et.y+30){
								return true;
							}
							if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30){
								return true;
							}
						}
						if(et.direct==1||et.direct==3){
							//如果敌人坦克往左或右
							if(this.x>=et.x&&this.x<=et.x+20&&this.y+20>et.y&&this.y+20<=et.y+30){
								return true;
							}
							if(this.x>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20){
								return true;
							}

						}
					}
				}
				break;
		}
		return b;
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(this.isLive){
			int sec=25-(int)(Math.random()*15);
			switch(this.direct){
			case 0:	//向上移动
				for(int i=0;i<sec;i++){
					if(y>0&&!this.isTouchTank()){
						y-=speed;
					}else
						break;
					try{
						Thread.sleep(100);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			
			case 1:
				for(int i=0;i<sec;i++){
					if(x<370&&!this.isTouchTank()){
						x+=speed;
					}else
						break;
					try{
						Thread.sleep(100);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			
			case 2:
				for(int i=0;i<sec;i++){
					if(y<250&&!this.isTouchTank()){
						y+=speed;
					}else
						break;
				
					try{
						Thread.sleep(100);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 3:
				x-=speed;
				for(int i=0;i<sec;i++){
					if(x>0&&!this.isTouchTank()){
						x-=speed;
					}else
						break;
					try{
						Thread.sleep(100);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				break;	
			}
			//随机产生新的方向；
			this.direct=(int)(Math.random()*4);		
			
			
			//
			times++;
			if(times>1){
				if(isLive){
					if(enemyBu.size()<5){
						Bullet bullet=null;
						switch(this.direct){
						
						case 0:
							//创建一颗子弹;
							bullet=new Bullet();
							bullet.setX(x+8);
							bullet.setY(y-6);
							bullet.setDirect(0);
							//把子弹加入到向量;
							enemyBu.add(bullet);
							break;
						case 1:
							bullet=new Bullet();
							bullet.setX(x+36);
							bullet.setY(y+8);
							bullet.setDirect(1);
							enemyBu.add(bullet);
							break;
						case 2:
							bullet=new Bullet();
							bullet.setX(x+8);
							bullet.setY(y+36);
							bullet.setDirect(2);
							enemyBu.add(bullet);
							break;
						case 3:
							bullet=new Bullet();
							bullet.setX(x-7);
							bullet.setY(y+8);
							bullet.setDirect(3);
							enemyBu.add(bullet);
							break;
						}
						Thread t=new Thread(bullet);
						t.start();
					}
				}
			}
		}
	
	}	
}
		
//子弹类
class Bullet implements Runnable{
	int x=0;
	int y=0;
	int direct=0;
	int speed=5;
	boolean isLive=true;
	public Bullet(){
		
	}
	public void run(){
		while(true){
			try{
			Thread.sleep(100);
			}catch(Exception e){
				
			}
			switch(this.direct){
				case 0:
					this.y-=speed;
					break;
				case 1:
					this.x+=speed;
					break;
				case 2:
					this.y+=speed;
					break;
				case 3:
					this.x-=speed;
					break;
			}
			//子弹何时死亡
			if(x>400||y>300||x<0||y<0){
				this.isLive=false;
				break;
			}
		}
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}
}

//爆炸类
class Bomb{
	int x=0;
	int y=0;
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y){
		this.x=x;
		this.y=y;
	}
	public void lifeDown(){
		if(life>0){
			life--;
		}else {
			isLive=false;
		}
	}
}