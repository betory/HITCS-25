//: 文件粉碎机

import java.awt.*;
import java.io.*;
import javax.swing.*;

//用户界面

public class FileMuller extends Frame
{
	TextField filename = new TextField();

	TextField directory = new TextField();

	Button open = new Button("打开");
	
	Button delete = new Button("删除");
	
	Button erase = new Button("销毁");
	
	Button exit = new Button("退出");

	JLabel title = new JLabel();

	Label info = new Label();
	
	Label information = new Label();
	public FileMuller()
	{
		setTitle("文件粉碎机");
		//加入文本框，以显示文件名、路径
		directory.setEditable(false);
		filename.setEditable(false);
		Panel p = new Panel();
		p.setLayout(new GridLayout(3, 1));
		p.add(title);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setText("<html><body><font color=red face=\"宋体\" size=3>警告：文件销毁之后将无法恢复！</font></body></html>");
		p.add(filename);
		p.add(directory);
		add("North", p);
		//加入文本显示区域，试图显示文件信息
		p = new Panel();
		p.setLayout(new GridLayout(1, 1));
		p.add(info);
		p.add(information);
		add(p);
		//加入按钮
		p = new Panel();
		p.setLayout(new FlowLayout());	
		p.add(open);
		p.add(delete);
		p.add(erase);
		p.add(exit);
		add("South", p);
	}

	//选择文件子窗口
	FileDialog d = new FileDialog(this, "请选择您想要粉碎的文件");//创建子窗口实例对象d

	String is;

	//定义选中文件信息：
	public String name = "";

	public String path = "";

	public String filepath = "";

	File MyFile;

	RandomAccessFile Destroy;

	private long seed; //产生随机数种子!

	private final static long multiplier = 0x5DEECE66DL;

	private final static long adder = 0xBL;

	private final static long mask = (1L << 48) - 1;

	//事件处理：
	//主窗口退出功能
	public boolean handleEvent(Event evt)
	{
		if (evt.id == Event.WINDOW_DESTROY)
			//System.exit(0);
			this.dispose();	//释放框架资源，我觉得这比上面那条退出语句效果好
		else
			return super.handleEvent(evt);
		return true;
	}

	//按钮动作
	public boolean action(Event evt, Object arg)
	{

		//“打开”操作
		if (evt.target.equals(open))
		{
			d.setFile("*.*"); // 文件类型过滤
			d.setDirectory("d:"); // Current directory
			d.setVisible(true);
			name = d.getFile();
			path = d.getDirectory();
			filepath = d.getDirectory() + d.getFile();
			is = d.getFile();//判断操作
			//判断是否选中文件
			if ((d.getFile()) != null)
			{
				MyFile = new File(filepath);
				filename.setText("已选择" + "    " + name);
				String size = String.valueOf(MyFile.length());
				directory.setText(filepath);
				info.setText("文件大小:" + size + "字节");
			} else
			{
				filename.setText("未选择任何文件！");
				directory.setText("");
				info.setText("");
			}
		}
		
		//“删除”操作
		else if (evt.target.equals(delete))
		{
			if (is != null)
			{
				if ((MyFile.canWrite() == true))
				{
					MyFile.delete();//删除文件
					filename.setText("请选择下一个需要删除的文件");
					directory.setText("");
					info.setText(name + "   " + "已成功删除！");
				}
			}	
		}
			
		//“销毁”操作
		else if (evt.target.equals(erase))
		{

			if (is != null)
			{
				if ((MyFile.canWrite() == true))
				{

					//产生每4字节一组的随机数写入文件

					try
					{/*//如果在循环外产生随机数，可以减少内存消耗
						seed = System.currentTimeMillis();
						if (seed == 0)
							seed = System.currentTimeMillis();

						//产生0到2147483647的随机整数
						seed = (seed * multiplier + adder) & mask;
						int NewData = (int) (seed >>> 17) % 2147483647;*/

						RandomAccessFile Destroy = new RandomAccessFile(MyFile,"rw");
						for (int j = 0; j <= 1; j++)
						{//循环写2次随机数
							for (int i = 0; i < ((MyFile.length()+3) / 4); i++)
							{ 
						  	//如果在循环内实时产生随机数，可以进一步提高安全性，但会消耗更多内存
						  	seed = System.currentTimeMillis();
						  	if (seed == 0)
						  		seed =System.currentTimeMillis();
						    	//产生0到2147483647的随机整数 
						    	seed = (seed * multiplier +adder) & mask;
						    	int NewData=(int)(seed >>> 17) %2147483647;
						  
								Destroy.writeInt(NewData);//写入新数据以破坏原文件
																
								//在information打印出生成的随机数，用于观察效果
								information.setText(NewData + " ");
							}
							Destroy.seek(0);
						}//循环结束，文件已破坏
						Destroy.close();
					} catch (IOException exc)//异常处理
					{
						info.setText("操作失败，请确保文件可读写！");
						System.out.println("文件不能被销毁，请检查相关属性！");
					}
					MyFile.delete();//删除文件
					filename.setText("请选择下一个需要粉碎的文件");
					directory.setText("");
					info.setText(name + "   " + "已成功销毁！");

				} else
				{
					filename.setText(" ");
					directory.setText(" ");
					info.setText("此文件不可写，请修改属性后再销毁!");
				}
				is = null;//操作已完成
			} else
			{
				info.setText("不能执行命令，请先选择需要销毁的文件!");
			}
		}

		//实现“退出“按钮
		else if (evt.target.equals(exit))
		{
			this.dispose();
		}


		//交还消息控制权，以完成事件响应机制		
		else
			return super.action(evt, arg);
		return true;
	}

	public static void main(String[] args)
	{
		Frame f = new FileMuller();
		f.setSize(500, 300);
		f.setResizable(false);//固定窗口大小，不可改变
		f.setVisible(true);
		
	}
}
