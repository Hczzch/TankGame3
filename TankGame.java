/*
 * 1.��һ��̹��
 * 2.�ҵ�̹�˿����ƶ�
 * 3.���������ӵ�(���5��)
 * 4.����̹�˺��б�ըЧ��,������ʧ(���۵��ұ����ж��ᱬը)��
 * 5.��ֹ̹���ص��˶�
 * 6.���Էֹ�		��һ����ʼ��panel�����ǿյ�  ������˸
 * 7.������ͣ�ͼ���
 * 8.���Լ�¼��ҳɼ�
 * 9.��������
 */
package TankGame3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TankGame extends JFrame implements ActionListener{
	Mypanel mp=null;
	StartPanel sp=null;
	//������Ҫ�Ĳ˵�
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
		//���ÿ�ݼ�
		jm1.setMnemonic('G');
		
		jmi1=new JMenuItem("New Game(N)");
		jmi1.setMnemonic('N');
		jmi1.setActionCommand("newgame");
		//Ҫ��jmi1��Ӧ��
		jmi1.addActionListener(this);
		
		jmi2=new JMenuItem("Exit(E)");
		jmi2.setMnemonic('E');
		jmi2.setActionCommand("exit");
		//��jmi2��Ӧ��
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
			//����ս�����
			
			mp=new Mypanel();
			Thread t=new Thread(mp);
			t.start();
			//ɾ��ԭ�������
			this.remove(sp);
			
			this.add(mp);
			this.addKeyListener(mp);
			this.setVisible(true);
		}
		else if(a.getActionCommand().equals("exit")){
			//�ȱ������˳�
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
//����һ����ʾ������
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



//�ҵ����
class Mypanel extends JPanel implements KeyListener,Runnable{
	//����һ��̹��
	Hero hero=null;
	int enNum=5;
	
	String flag="newgame";
	
	//��������ͼƬ
	Image im1=null;
	Image im2=null;
	Image im3=null;
	
	Vector<Enemies> enem=new Vector<Enemies>();	
	Vector<Bomb> bomb=new Vector<Bomb>();
	public Mypanel(){
		//�ָ���¼
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
		//��ʼ��ͼƬ��
		im1=new ImageIcon("images/ͼƬ1.png").getImage();
		im2=new ImageIcon("images/ͼƬ2.png").getImage();
		im3=new ImageIcon("images/ͼƬ3.png").getImage();
		
		
	}
	
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0,0,400,300);	//����
		
		
		//������ʾ��Ϣ
		this.showInfo(g);
		
		
		//���Լ�̹��
		if(hero.isLive){
			this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
		}
		else{
			
		}
		
		
		//���з�̹��
		for(int i=0;i<enem.size();i++){
			Enemies enen=enem.get(i);
			if(enen.isLive){
				this.drawTank(enen.getX(),enen.getY(), g, enen.getDirect(), 1);
			}
			//������̹���ӵ�
			for(int j=0;j<enen.enemyBu.size();j++){
				Bullet enBu=enen.enemyBu.get(j);
				if(enBu.isLive){
					g.setColor(Color.RED);
					g.fill3DRect(enBu.x,enBu.y,3,3,false);
				}else 		//��������ʹ�������ɾ��
					enen.enemyBu.remove(enBu);
			}
		}
		
		
		
		//������ը
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
	
	
	//������ʾ��Ϣ̹�ˣ�
	public void showInfo(Graphics g){
			this.drawTank(80, 330, g, 0, 0);
			g.setColor(Color.BLACK);
			g.drawString(Recorder.getMyLife()+" ",110,350);
			
			this.drawTank(250, 330, g, 0, 1);
			g.setColor(Color.BLACK);
			g.drawString(Recorder.getEnNum()+" ", 280, 350);
			
			
			//������ҵ��ܳɼ�
			g.setColor(Color.black);
			Font f=new Font("����",Font.BOLD,20);
			g.setFont(f);
			g.drawString("�ܷ���",460,30);
	
			this.drawTank(420,60,g,0,0);
			g.setColor(Color.black);
			g.drawString(Recorder.getAllenNum()+" ", 450, 80);
	}
	
	//�ж��ӵ�����̹��
	public void hitEnemies(){
		for(int i=0;i<hero.bu.size();i++){
			Bullet bull=hero.bu.get(i);
			if(bull.isLive){
				for(int j=0;j<enem.size();j++){
					//ȡ��tank
					Enemies en=enem.get(j);
					if(en.isLive){
						this.hitTank(bull, en);
						if(!en.isLive){
							//���Ҽ��ٵ�������1
							Recorder.reduceEnNum();
							//���ӷ���(ս��)
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
						//�����Լ���������1
						Recorder.reduceMylife();
					
					}
				}
			}
		}
	}
	
	//����tank
	public void hitTank(Bullet b,Tank tank){
		//�ж�̹�˵ķ���
		if(tank.isLive){
		switch(tank.direct){
			//��������
			case 0:
			case 2:
				if(b.x>=tank.x&&b.x<=tank.x+20&&b.y>=tank.y&&b.y<=tank.y+30){
					//���У��ӵ���������������
					b.isLive=false;
					tank.isLive=false;
					
					
					Bomb b1=new Bomb(tank.getX(),tank.getY());
					bomb.add(b1);
					break;
				}
				
			case 1:
			case 3:
				if(b.x>tank.x&&b.x<tank.x+30&&b.y>tank.y&&b.y<tank.y+20){
					//���У��ӵ���������������
					b.isLive=false;
					tank.isLive=false;
					
					
					Bomb b1=new Bomb(tank.getX(),tank.getY());
					bomb.add(b1);
					break;
				}
		}
		}
		
		
		
	}
	//����̹�˵ĺ���
	public void drawTank(int x,int y,Graphics g,int direct,int type){
		switch(type){
			case 0:		//�Լ�
				g.setColor(Color.ORANGE);
				break;
			case 1:		//����
				g.setColor(Color.GREEN);
				break;
		}
		
		switch(direct){
			case 0:		//����
				//1.������ߺ��ұߵľ���
				g.fill3DRect(x, y, 5, 30,false);
				g.fill3DRect(x+15, y, 5, 30,false);
				//2.�����м����
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//3.����Բ��
				g.fillOval(x+4, y+9, 10, 10);
				//4.��ֱ��
				g.drawLine(x+9, y+15, x+9, y-5);
				break;
			case 1:		//����
				g.fill3DRect(x, y, 30, 5, false);
				g.fill3DRect(x, y+15, 30, 5, false);
				g.fill3DRect(x+5,y+5,20,10,false);
				g.fillOval(x+9, y+4, 10, 10);
				g.drawLine(x+15, y+9, x+35, y+9);
				break;
			case 2:		//����
				//1.������ߺ��ұߵľ���
				g.fill3DRect(x, y, 5, 30,false);
				g.fill3DRect(x+15, y, 5, 30,false);
				//2.�����м����
				g.fill3DRect(x+5, y+5, 10, 20,false);
				//3.����Բ��
				g.fillOval(x+4, y+9, 10, 10);
				//4.��ֱ��
				g.drawLine(x+9, y+15, x+9, y+35);
				break;
			case 3:		//����
				g.fill3DRect(x, y, 30, 5, false);
				g.fill3DRect(x, y+15, 30, 5, false);
				g.fill3DRect(x+5,y+5,20,10,false);
				g.fillOval(x+9, y+4, 10, 10);
				g.drawLine(x+15, y+9, x-5, y+9);
				break;
		}
		
		
		//��bu��ȡ��ÿ���ӵ���������
		
		for(int i=0;i<hero.bu.size();i++){
			Bullet bullet1=hero.bu.get(i);
			//����һ���ӵ���
			if(bullet1!=null&&bullet1.isLive==true){
				g.setColor(Color.RED);
				g.fill3DRect(bullet1.x,bullet1.y,3,3,false);
			}
			if(bullet1.isLive==false){
				this.hero.bu.remove(bullet1);
			}
		}
		
}
	@Override	//wsad������������
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(this.hero.isLive){
		if(arg0.getKeyCode()==KeyEvent.VK_W){
			//�����ҵ�̹�˵ķ���
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
		//�����ػ�Panel��
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
			//�ж��Լ��Ƿ���е���̹��
			this.hitEnemies();
			//�ж��Լ��Ƿ񱻵��˻���
			this.hitHeros();
			
		this.repaint();
		}
	}
}
