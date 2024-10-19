import javax.swing.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;


class Calculator
{
	JFrame frame=new JFrame();
	JTextField text=new JTextField();
	JButton button;
	JPanel panel1=new JPanel(); 
	JPanel panel2=new JPanel();
	String bts[]={"1","2","3","+","4","5","6","-","7","8","9","*",".","0","=","/"};
	
	void show()
	{
		int i;
		frame.add(panel1);
		frame.add(panel2);
		panel1.add(text);
		Calculate cal=new Calculate(text);
		for(i=0;i<bts.length;i++)
		{
			button=new JButton(""+bts[i]);
			button.setFont(new Font("Times",Font.PLAIN,40));
			panel2.add(button);
			button.addActionListener(cal);
			if(i==14)
			{	
				button.setBackground(new Color(230, 196, 156));
				button.setForeground(new Color(56, 43, 28));
			}
			else
			{	
				button.setBackground(new Color(190, 207, 235));
				button.setForeground(new Color(47, 74, 79));
			}
		}
		text.setFont(new Font("Times new roman",Font.PLAIN,30));
		text.setBackground(new Color(226, 227, 213));
		text.setForeground(new Color(33, 36, 2));
		panel1.setBounds(5,3,377,50);
		panel2.setBounds(5,55,377,405);
		frame.setLayout(null);
		panel1.setLayout(new GridLayout(1,0));
		panel2.setLayout(new GridLayout(4,3,5,5));
		frame.setSize(400,500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String [] args)
	{
		new Calculator().show();
	}
	
}


class Calculate implements ActionListener
{
	JTextField text;
	String scr="";
	int lt,i,j,k;
	String arxp[];
	String pofxp[];
	
	Calculate(JTextField text)
	{
		this.text=text;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JButton jb=(JButton)e.getSource();
		//JTextField txt=(JTextField)e.getSource();
		String s=jb.getText();
		scr=scr+s;
		if(s.equals("="))
		{
			arrange();
			TheOffice ob=new TheOffice(arxp);
			ob.putInStack();
			text.setText(ob.postfixCal());
			scr="";
		}
		else
			text.setText(scr);
	}
	
	void arrange()
	{
		
		String[] strs=scr.split("[+\\*=/-]");
		i=0;
		j=0;
		k=0; 
		char exp[]=scr.toCharArray();
		lt=scr.length();
		arxp=new String[lt];
		while(i<lt)
		{
			if(exp[i]=='+')
			{
				arxp[j]=strs[k];
				j++;
				k++;
				arxp[j]="+";
				j++;
			}
			else if(exp[i]=='*')
			{
				arxp[j]=strs[k];
				j++;
				k++;
				arxp[j]="*";
				j++;
			}
			else if(exp[i]=='-')
			{
				arxp[j]=strs[k];
				j++;
				k++;
				arxp[j]="-";
				j++;
			}
			else if(exp[i]=='/')
			{
				arxp[j]=strs[k];
				j++;
				k++;
				arxp[j]="/";
				j++;
			}
			i++;
		}
		arxp[j]=strs[k];
	}
	
}


class TheOffice
{
	String exp[];
	String posexp[]=new String[50];
	int i,j=0;
	
	TheOffice(String lp[])
	{
		exp=lp;
	}
	
	void putInStack()
	{
		int l=0;
		for(i=0;exp[i]!=null;i++)
			l++;
		Symbol expWithPres[]=new Symbol[l];
		StackForExp stc=new StackForExp();
		for(i=0;i<l;i++)
		{
			if(exp[i].equals("+") || exp[i].equals("-"))
			{
				expWithPres[i]=new Symbol(exp[i],1,1);
			}
			else if(exp[i].equals("*") || exp[i].equals("/"))
			{
				expWithPres[i]=new Symbol(exp[i],2,1);
			}
			else
			{
				expWithPres[i]=new Symbol(exp[i]);
			}
		}
		for(i=0;i<l;i++)
		{
			if((expWithPres[i].sym).equals("+") || (expWithPres[i].sym).equals("-") || (expWithPres[i].sym).equals("*") || (expWithPres[i].sym).equals("/"))
			{
				while((stc.isempty())>-1 &&   expWithPres[i].prec<=(stc.peek()))
				{
					posexp[j++]=stc.pop();
				}
				stc.push(expWithPres[i]);
			}
			else
			{
				posexp[j++]=expWithPres[i].sym;
			}
		}
		while((stc.isempty())>-1)
		{
			posexp[j++]=stc.pop();
		}
	}
	
	String postfixCal()
	{
		StackForExp stcev=new StackForExp();
		double v1,v2,v3;
		for(int i=0;posexp[i]!=null;i++)
		{
			if(posexp[i].equals("+") || posexp[i].equals("-") || posexp[i].equals("*") || posexp[i].equals("/"))
			{
				v1=Double.parseDouble(stcev.pop());
				v2=Double.parseDouble(stcev.pop());
				if(posexp[i].equals("+"))
				{
					v3=v1+v2;
					stcev.poosh(v3+"");
				}
				if(posexp[i].equals("-"))
				{
					v3=v2-v1;
					stcev.poosh(v3+"");
				}
				if(posexp[i].equals("*"))
				{
					v3=v2*v1;
					stcev.poosh(v3+"");
				}
				if(posexp[i].equals("/"))
				{
					v3=v2/v1;
					stcev.poosh(v3+"");
				}
			}
			else
			{
				stcev.poosh(posexp[i]);
			}
		}
		return(stcev.pop());
	}
	
}


class Symbol
{
	String sym;
	int prec, ass;
	
	Symbol(String x,int p, int a)
	{
		sym=x;
		prec=p;
		ass=a;
	}
	
	Symbol(String x)
	{
		sym=x;
	}
	
	Symbol(Symbol s)
	{
		this.sym = s.sym;
		this.prec = s.prec;
		this.ass = s.ass;
	}
	
}


class StackForExp
{
	Symbol stackforcal[]=new Symbol[50];
	int top=-1;
	
	void push(Symbol x)
	{
		stackforcal[++top]=new Symbol(x);
	}
	
	void poosh(String x)
	{
		stackforcal[++top]=new Symbol(x);
	}
	
	String pop()
	{
		return(stackforcal[top--].sym);
	}
	
	int peek()
	{
		return(stackforcal[top].prec);
	}
	
	int isempty()
	{
		return top;
	}
	
}