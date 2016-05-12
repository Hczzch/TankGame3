/*
 * 1.画一个坦克
 * 2.我的坦克可以移动
 * 3.可以连发子弹(最多5颗)
 * 4.击中坦克后有爆炸效果,并且消失(无论敌我被击中都会爆炸)；
 * 5.防止坦克重叠运动
 * 6.可以分关		做一个开始的panel，它是空的  字体闪烁
 * 7.可以暂停和继续
 * 8.可以记录玩家成绩
 * 9.加入声音
 */
package TankGame3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TankGame extends JFrame implements ActionListener{
	Mypanel mp=null;
	StartPanel sp=null;
	//做出需要的菜单
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankGame a=new TankGame();
	}
	public TankGame(){

		jmb=new JMenuBar();
		jm1=new JMenu("Game(G)");
		//设置快捷键
		jm1.setMnemonic('G');
		
		jmi1=new JMenuItem("New Game(N)");
		jmi1.setMnemonic('N');
		jmi1.setActionCommand("newgame");
		//要对jmi1响应；
		jmi1.addActionListener(this);
		
		jmi2=new JMenuItem("Exit(E)");
		jmi2.setMnemonic('E');
		jmi2.setActionCommand("exit");
		//对jmi2响应；
		jmi2.addActionListener(this);
		
		jmi3=new JMenuItem("Save and Exit(S)");
		jmi3.setMnemonic('S');
		jmi3.setActionCommand("save");
		jmi3.addActionListener(this);
		
		jmi4=new JMenuItem("Continune(C)");
		jmi4.setMnemonic('C');
		jmi4.setActionCommand("con");
		jmi4.addActionListener(this);
		
		jm1.add(jmi1);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jm1.add(jmi2);
		jmb.add(jm1);
		
		
		sp=new StartPanel();
		Thread t=new Thread(sp);
		t.start();
		this.setJMenuBar(jmb);
		this.add(sp);
		this.setResizable(false);
		this.setSize(600,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent a) {
		// TODO Auto-generated method stub
		if(a.getActionCommand().equals("newgame")){
			//创建战场面板
			
			mp=new Mypanel();
			Thread t=new Thread(mp);
			t.start();
			//删掉原来的面板
			this.remove(sp);
			
			this.add(mp);
			this.addKeyListener(mp);
			this.setVisible(true);
		}
		else if(a.getActionCommand().equals("exit")){
			//先保存在退出
			Recorder.keepRecording();
			System.exit(0);
		}
		else if(a.getActionCommand().equals("save")){
			Recorder rd=new Recorder();
			rd.setEts(mp.enem);
			rd.keepRecAndEnemyTank();
			System.exit(0);
		}
		else if(a.getActionCommand().equals("con")){
			
		}
	}
}
//就是一个提示的作用
class StartPanel extends JPanel implements Runnable{
	int times=0;
	
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		if(times%2==0){
			g.setColor(Color.YELLOW);
			Font myfont=new Font("",Font.BOLD,30);
			g.setFont(myfont);
			g.drawString("STAGE 1", 140, 140);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
				times++;
			
				
			
			this.repaint();
		}
	}
}



//我的面板
class Mypanel extends JPanel implements KeyListener,Runnable{
	//定义一个坦克
	Hero hero=null;
	int enNum=5;
	
	String flag="newgame";
	
	//定义三张图片
	Image im1=null;
	Image im2=null;
	Image im3=null;
	
	Vector<Enemies> enem=new Vector<Enemies>();	
	Vector<Bomb> bomb=new Vector<Bomb>();
	public Mypanel(){
		//恢复记录
		Recorder.getRecording();
		
		hero=new Hero(120,210);
		//
		for(int i=0;i<enNum;i++){
			Enemies enemy=new Enemies((i+1)*60,10);
			enem.add(enemy);
			enemy.setEnemy(enem);
			Bullet enBu=new Bullet();
			enemy.enemyBu.add(enBu);
			Thread t1=new Thread(enemy);
			Thread t2=new Thread(enBu);
			t1.start();
			t2.start();
		}
		//初始化图片；
		im1=new ImageIcon("images/图片1.png").getImage();
		im2=new ImageIcon("images/图片2.png").getImage();
		im3=new ImageIcon("images/图片3.png").getImage();
		
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0,0,400,300);	//背景
		
		
		//画出提示信息
		this.showInfo(g);
		
		
		//画自己坦克
		if(hero.isLive){
			this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
		}
		else{
			
		}
		
		
		//画敌方坦克
		for(int i=0;i<enem.size();i++){
			Enemies enen=enem.get(i);
			if(enen.isLive){
				this.drawTank(enen.getX(),enen.getY(), g, enen.getDirect(), 1);
			}
			//画敌人坦克子弹
			for(int j=0;j<enen.enemyBu.size();j++){
				Bullet enBu=enen.enemyBu.get(j);
				if(enBu.isLive){
					g.setColor(Color.RED);
					g.fill3DRect(enBu.x,enBu.y,3,3,false);
				}else 		//如果死亡就从向量里删除
					enen.enemyBu.remove(enBu);
			}
		}
		
		
		
		//画出爆炸
		for(int i=0;i<bomb.size();i++){
			
			Bomb b=bomb.get(i);
			if(b.life>6){
				g.drawImage(im3, b.x, b.y, 30,30,this);
			}
			else if(b.life>3){
				g.drawImage(im2, b.x, b.y, 30,30,this);
			}
			else{
				g.drawImage(im1, b.x, b.y, 30,30,this);
			}
			b.lifeDown();
			if(b.life==0){
				bomb.remove(b);
			}
		}
		
	}
	
	
	//画出提示信息坦克；
	public void showInfo(Graphics g){
			this.drawTank(80, 330, g, 0, 0);
			g.setColor(Color.BLACK);
			g.drawString(Recorder.getMyLife()+" ",110,350);
			
			this.drawTank(250, 330, g, 0, 1);
			g.setColor(Color.BLACK);
			g.drawString(Recorder.getEnNum()+" ", 280, 350);
			
			
			//画出玩家的总成绩
			g.setColor(Color.black);
			Font f=new Font("宋体",Font.BOLD,20);
			g.setFont(f);
			g.drawString("总分数",460,30);
	
			this.drawTank(420,60,g,0,0);
			g.setColor(Color.black);
			g.drawString(Recorder.getAllenNum()+" ", 450, 80);
	}
	
	//判断子弹击中坦克
	public void hitEnemies(){
		for(int i=0;i<hero.bu.size();i++){
			Bullet bull=hero.bu.get(i);
			if(bull.isLive){
				for(int j=0;j<enem.size();j++){
					//取出tank
					Enemies en=enem.get(j);
					if(en.isLive){
						this.hitTank(bull, en);
						if(!en.isLive){
							//并且减少敌人数量1
							Recorder.reduceEnNum();
							//增加分数(战绩)
							Recorder.addEnNum();
							
						}
					}
				}
			}
		
		}	
	}
	public void hitHeros(){
		for(int i=0;i<enem.size();i++){
			Enemies en=enem.get(i);
			for(int j=0;j<en.enemyBu.size();j++){
				Bullet bu=en.enemyBu.get(j);
				if(hero.isLive){
					this.hitTank(bu, hero);
					if(!hero.isLive){
						//减少自己生命数量1
						Recorder.reduceMylife();
					
					}
				}
			}
		}
	}
	
	//击中tank
	public void hitTank(Bullet b,Tank tank){
		//判断坦克的方向
		if(tank.isLive){
		switch(tank.direct){
			//向上向下
			case 0:
			case 2:
				if(b.x>=tank.x&&b.x<=tank.x+20&&b.y>=tank.y&&b.y<=tank.y+30){
					//击中，子弹死亡，敌人死亡
					b.isLive=false;
					tank.isLive=false;
					
					
					Bomb b1=new Bomb(tank.getX(),tank.getY());
					bomb.add(b1);
					break;
				}
				
			case 1:
			case 3:
				if(b.x>tank.x&&b.x<tank.x+30&&b.y>tank.y&&b.y<tank.y+20){
					//击中，子弹死亡，敌人死亡
					b.isLive=false;
					tank.isLive=false;
					
					
					Bomb b1=new Bomb(tank.getX(),tank.getY());
					bomb.add(b1);
					break;
				}
		}
		}
		
		
		
	}
	//画出坦克的函数
	public void drawTank(int x,int y,Graphics g,int direct,int type){
		switch(type){
			case 0:		//自己
				g.setColor(Color.ORANGE);
				break;
			case 1:		//敌人
				g.setColor(Color.GREEN);
				break;
		}
		
		switch(direct){
			case 0:		//向上
				//1.画出左边和右边的矩形
				g.fill3DRect(x, y, 5, 30,false);
				g.fill3DRect(x+15, y, 5, 30,false);
				//2.画出中间矩形
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//3.画出圆形
				g.fillOval(x+4, y+9, 10, 10);
				//4.画直线
				g.drawLine(x+9, y+15, x+9, y-5);
				break;
			case 1:		//向右
				g.fill3DRect(x, y, 30, 5, false);
				g.fill3DRect(x, y+15, 30, 5, false);
				g.fill3DRect(x+5,y+5,20,10,false);
				g.fillOval(x+9, y+4, 10, 10);
				g.drawLine(x+15, y+9, x+35, y+9);
				break;
			case 2:		//向下
				//1.画出左边和右边的矩形
				g.fill3DRect(x, y, 5, 30,false);
				g.fill3DRect(x+15, y, 5, 30,false);
				//2.画出中间矩形
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//3.画出圆形
				g.fillOval(x+4, y+9, 10, 10);
				//4.画直线
				g.drawLine(x+9, y+15, x+9, y+35);
				break;
			case 3:		//向左
				g.fill3DRect(x, y, 30, 5, false);
				g.fill3DRect(x, y+15, 30, 5, false);
				g.fill3DRect(x+5,y+5,20,10,false);
				g.fillOval(x+9, y+4, 10, 10);
				g.drawLine(x+15, y+9, x-5, y+9);
				break;
		}
		
		
		//从bu中取出每颗子弹，并画出
		
		for(int i=0;i<hero.bu.size();i++){
			Bullet bullet1=hero.bu.get(i);
			//画出一颗子弹；
			if(bullet1!=null&&bullet1.isLive==true){
				g.setColor(Color.RED);
				g.fill3DRect(bullet1.x,bullet1.y,3,3,false);
			}
			if(bullet1.isLive==false){
				this.hero.bu.remove(bullet1);
			}
		}
		
}
	@Override	//wsad代表上下左右
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(this.hero.isLive){
		if(arg0.getKeyCode()==KeyEvent.VK_W){
			//设置我的坦克的方向
			this.hero.setDirect(0);
			this.hero.moveUp();
		}
		else if(arg0.getKeyCode()==KeyEvent.VK_D){
			this.hero.setDirect(1);
			this.hero.moveRight();
		}
		else if(arg0.getKeyCode()==KeyEvent.VK_S){
			this.hero.setDirect(2);
			this.hero.moveDown();
		}
		else if(arg0.getKeyCode()==KeyEvent.VK_A){
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		if(arg0.getKeyCode()==KeyEvent.VK_J){
				if(this.hero.bu.size()<5){
					this.hero.shot();
			}
		}
		//必须重绘Panel；
		}
		this.repaint();
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void run(){
		while (true){
			try{
				Thread.sleep(100);
			}catch (Exception e){
			
			}
			//判断自己是否击中敌人坦克
			this.hitEnemies();
			//判断自己是否被敌人击中
			this.hitHeros();
			
		this.repaint();
		}
	}
}
