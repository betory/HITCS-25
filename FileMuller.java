//�ļ������

import java.awt.*;
import java.io.*;
import javax.swing.*;

//�û�����

public class FileMuller extends Frame
{
	TextField filename = new TextField();

	TextField directory = new TextField();

	Button open = new Button("��");
	
	Button delete = new Button("ɾ��");
	
	Button erase = new Button("����");
	
	Button exit = new Button("�˳�");

	JLabel title = new JLabel();

	Label info = new Label();
	
	Label information = new Label();
	public FileMuller()
	{
		setTitle("�ļ������");
		//�����ı�����ʾ�ļ�����·��
		directory.setEditable(false);
		filename.setEditable(false);
		Panel p = new Panel();
		p.setLayout(new GridLayout(3, 1));
		p.add(title);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setText("<html><body><font color=red face=\"����\" size=3>���棺�ļ�����֮���޷��ָ���</font></body></html>");
		p.add(filename);
		p.add(directory);
		add("North", p);
		//�����ı���ʾ������ʾ�ļ���Ϣ
		p = new Panel();
		p.setLayout(new GridLayout(1, 1));
		p.add(info);
		p.add(information);
		add(p);
		//���밴ť
		p = new Panel();
		p.setLayout(new FlowLayout());	
		p.add(open);
		p.add(delete);
		p.add(erase);
		p.add(exit);
		add("South", p);
	}

	//ѡ���ļ��Ӵ���
	FileDialog d = new FileDialog(this, "��ѡ������Ҫ������ļ�");//�����Ӵ���ʵ������

	String is;

	//����ѡ���ļ���Ϣ��
	public String name = "";

	public String path = "";

	public String filepath = "";

	File MyFile;

	RandomAccessFile Destroy;

	private long seed; //�������������!

	private final static long multiplier = 0x5DEECE66DL;

	private final static long adder = 0xBL;

	private final static long mask = (1L << 48) - 1;
	
	//�¼�����
	//�������˳�����
	public boolean handleEvent(Event evt)
	{
		if (evt.id == Event.WINDOW_DESTROY)
			//System.exit(0);
			this.dispose();	//�ͷſ����Դ
		else
			return super.handleEvent(evt);
		return true;
	}

	//��ť����
	public boolean action(Event evt, Object arg)
	{

		//���򿪡�����
		if (evt.target.equals(open))
		{
			d.setFile("*.*"); // �ļ����͹���
			d.setDirectory("d:"); // Current directory
			d.setVisible(true);
			name = d.getFile();
			path = d.getDirectory();
			filepath = d.getDirectory() + d.getFile();
			is = d.getFile();//�жϲ���
			//�ж��Ƿ�ѡ���ļ�
			if ((d.getFile()) != null)
			{
				MyFile = new File(filepath);
				filename.setText("��ѡ��" + "    " + name);
				String size = String.valueOf(MyFile.length());
				directory.setText(filepath);
				info.setText("�ļ���С:" + size + "�ֽ�");
			} else
			{
				filename.setText("δѡ���κ��ļ���");
				directory.setText("");
				info.setText("");
			}
		}
		
		//��ɾ��������
		else if (evt.target.equals(delete))
		{
			if (is != null)
			{
				if ((MyFile.canWrite() == true))
				{
					MyFile.delete();//ɾ���ļ�
					filename.setText("��ѡ����һ����Ҫɾ�����ļ�");
					directory.setText("");
					info.setText(name + "   " + "�ѳɹ�ɾ����");
				}
			}	
		}
			
		//�����١�����
		else if (evt.target.equals(erase))
		{

			if (is != null)
			{
				if ((MyFile.canWrite() == true))
				{

					//����ÿ4�ֽ�һ��������д���ļ�

					try
					{/*//�����ѭ�����������������Լ����ڴ�����
						seed = System.currentTimeMillis();
						if (seed == 0)
							seed = System.currentTimeMillis();

						//����0��2147483647���������
						seed = (seed * multiplier + adder) & mask;
						int NewData = (int) (seed >>> 17) % 2147483647;*/

						RandomAccessFile Destroy = new RandomAccessFile(MyFile,"rw");
						for (int j = 0; j <= 1; j++)
						{//ѭ��д2�������
							for (int i = 0; i < ((MyFile.length()+3) / 4); i++)
							{ 
						  	//�����ѭ����ʵʱ��������������Խ�һ����߰�ȫ�ԣ��������ĸ����ڴ�
						  	seed = System.currentTimeMillis();
						  	if (seed == 0)
						  		seed =System.currentTimeMillis();
						    	//����0��2147483647��������� 
						    	seed = (seed * multiplier +adder) & mask;
						    	int NewData=(int)(seed >>> 17) %2147483647;
						  
								Destroy.writeInt(NewData);//д�����������ƻ�ԭ�ļ�
																
								//��information��ӡ�����ɵ�����������ڹ۲�Ч��
								information.setText(NewData + " ");
							}
							Destroy.seek(0);
						}//ѭ���������ļ����ƻ�
						Destroy.close();
					} catch (IOException exc)//�쳣����
					{
						info.setText("����ʧ�ܣ���ȷ���ļ��ɶ�д��");
						System.out.println("�ļ����ܱ����٣�����������ԣ�");
					}
					MyFile.delete();//ɾ���ļ�
					filename.setText("��ѡ����һ����Ҫ������ļ�");
					directory.setText("");
					info.setText(name + "   " + "�ѳɹ����٣�");

				} else
				{
					filename.setText(" ");
					directory.setText(" ");
					info.setText("���ļ�����д�����޸����Ժ�������!");
				}
				is = null;//���������
			} else
			{
				info.setText("����ִ���������ѡ����Ҫ���ٵ��ļ�!");
			}
		}

		//ʵ�֡��˳�����ť
		else if (evt.target.equals(exit))
		{
			this.dispose();
		}


		//������Ϣ����Ȩ��������¼���Ӧ����		
		else
			return super.action(evt, arg);
		return true;
	}

	public static void main(String[] args)
	{
		Frame f = new FileMuller();
		f.setSize(500, 300);
		f.setResizable(false);//�̶����ڴ�С�����ɸı�
		f.setVisible(true);
		
	}
}
