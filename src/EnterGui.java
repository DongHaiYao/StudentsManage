import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterGui extends JFrame {//程序入口
    private JPanel panel_1 = new JPanel();
    private JButton btnConfim = new JButton("登陆");
    private JButton btnSignUp = new JButton("注册");
    private JTextField userName = new JTextField(20);
    private JPasswordField pass = new JPasswordField(20);
    private JLabel userLaber = new JLabel("用户名");
    private  JLabel passLaber = new JLabel("密码");
    private JLabel text = new JLabel("欢迎使用学生成绩管理系统！");
    private ConnetDB conn = new ConnetDB();
    private ImageIcon icon=new ImageIcon("D:/icon.jpg");
    public static void main(String[] args) {
        EnterGui enterGui=new EnterGui();
     }
     public EnterGui(){
         super();
         this.setIconImage(icon.getImage());
         this.setTitle("学生成绩管理系统");
         this.setBounds(650, 250, 500, 400);
         this.setVisible(true);
         this.setResizable(false);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setLayout(null);
         panel_1.setLayout(null);
         panel_1.setBounds(0, 50, 600, 250);
         text.setBounds(140, 0, 250, 50);
         btnConfim.setBounds(150, 130, 70, 30);
         btnSignUp.setBounds(250,130,70,30);
         userLaber.setBounds(95, 50, 40, 30);
         passLaber.setBounds(95, 80, 40, 30);
         userName.setBounds(135, 50, 200, 30);
         pass.setBounds(135, 80, 200, 30);
         panel_1.add(text);
         panel_1.add(userLaber);
         panel_1.add(userName);
         panel_1.add(passLaber);
         panel_1.add(pass);
         panel_1.add(btnConfim);
         panel_1.add(btnSignUp);
         this.add(panel_1);
         setActionLintener();
    }
       private void setActionLintener(){
        btnConfim.addActionListener(new ActionListener() {
            //内部类实现主界面
            class MainGUI extends JFrame{
                final int COLUMN=10;
                private final List<String> TITLE= Arrays.asList("姓名","学号","教师","院系","英语成绩","高数成绩",
                        "大物成绩","平均分","最高分","最低分");
                Vector<Vector<String>> dataModel=new Vector<>();
                private JMenuBar bar=new JMenuBar();
                private JMenu menu_stu =new JMenu("管理学生成绩");
                private JMenu menu_course =new JMenu("课程成绩排名");
                private JMenu menu_exUser =new JMenu("退出登陆");
                private JMenuItem showAll =new JMenuItem("显示所有学生成绩");
                private JMenuItem addStu =new JMenuItem("添加学生");
                private JMenuItem findByName =new JMenuItem("按关键字查询");
                private JMenuItem showEng =new JMenuItem("按英语成绩排名");
                private JMenuItem showMath =new JMenuItem("按高数成绩排名");
                private JMenuItem showPhy =new JMenuItem("按大物成绩排名");
                private JMenuItem userEx =new JMenuItem("退出登录");
                private JButton btnDelete =new JButton("删除此条");
                private JButton btnChange =new JButton("修改此条");
                private JTable table;
                private ConnetDB conn=new ConnetDB();
                public MainGUI(){
                    super();
                    this.setTitle("学生成绩管理系统");
                    this.setBounds(650,250,800,500);
                    this.setVisible(true);
                    this.setResizable(false);
                    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    this.setIconImage(icon.getImage());
                    //菜单栏
                    menu_stu.add(showAll);
                    menu_stu.add(addStu);
                    menu_stu.add(findByName);
                    menu_course.add(showEng);
                    menu_course.add(showMath);
                    menu_course.add(showPhy);
                    menu_exUser.add(userEx);
                    bar.add(menu_stu);
                    bar.add(menu_course);
                    bar.add(menu_exUser);
                    this.setJMenuBar(bar);

                    Vector<String> titles=new Vector<>(TITLE);
                    table=new JTable(dataModel,titles);
                    table.getTableHeader().setReorderingAllowed(false);//表头不可拖动
                    for (int i = 0; i < COLUMN; i++) {
                        if(1==i||i==3){//这2列数据更长一些
                            table.getColumnModel().getColumn(i).setPreferredWidth(150);
                        }
                    }
                    //承载table的panel
                    JPanel tablePanel=new JPanel();
                    tablePanel.setLayout(new BoxLayout(tablePanel,BoxLayout.Y_AXIS));
                    this.add(tablePanel);
                    JScrollPane jScrollPane=new JScrollPane();
                    jScrollPane.setViewportView(table);
                    tablePanel.add(jScrollPane, BorderLayout.CENTER);
                    tablePanel.add(btnChange);
                    tablePanel.add(btnDelete);
                    tablePanel.updateUI();
                    setActionListener();
                }
                //监听事件
                private void setActionListener(){
                    btnChange.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int row = table.getSelectedRow();
                            int column = table.getSelectedColumn();
                            if (row == -1 || column == 0) return;
                            String val = dataModel.get(row).get(column);
                            String name = dataModel.get(row).get(0);
                            //解决表列名和table列名不一样
                            String convert="";
                            if(TITLE.get(column).equals("学号")) {
                                convert = "number";
                            }else if (TITLE.get(column).equals("教师")) {
                                convert = "teacher";
                            }else if (TITLE.get(column).equals("院系")) {
                                convert = "major";
                            }else if (TITLE.get(column).equals("英语成绩")) {
                                convert = "english";
                            }else if (TITLE.get(column).equals("高数成绩")) {
                                convert = "math";
                            }else if (TITLE.get(column).equals("大物成绩")) {
                                convert = "physic";
                            }
                            String sql = "update studentI set " + convert + " = ? where name = '"+name+"';";
                            PreparedStatement ps;
                            try {
                                ps = conn.getConnect().prepareStatement(sql);
                                if (TITLE.get(column).equals("学号")||TITLE.get(column).equals("英语成绩")||
                                        TITLE.get(column).equals("高数成绩")||
                                TITLE.get(column).equals("大物成绩")) {
                                    ps.setInt(1, Integer.parseInt(val));
                                }
                                else {
                                    ps.setString(1, val);
                                }
                                ps.executeUpdate();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            table.validate();
                            table.updateUI();
                        }
                    });
                    btnDelete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int row = table.getSelectedRow();
                            String sname = dataModel.get(row).get(0);
                            String sql = "delete from studentI where name = '" + sname + "';";
                            try {
                                if (conn.getConnect().createStatement().executeUpdate(sql) == 0) return;
                                dataModel.remove(row);
                                //更新表格
                                table.validate();
                                table.updateUI();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    showAll.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dataModel.clear();
                           Statement stmt;
                            try {
                                stmt =conn.getConnect().createStatement();
                                ResultSet rs = stmt.executeQuery("select *from studentI");
                                initTable(rs);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            //更新表格
                            table.validate();
                            table.updateUI();
                        }
                    });
                    addStu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            class AddStu extends JFrame {
                                ConnetDB conn = new ConnetDB();
                                private JTextField txName=new JTextField();
                                private JTextField txNo=new JTextField();
                                private JTextField txTeacher=new JTextField();
                                private JTextField txCol=new JTextField();
                                private JTextField txEng=new JTextField();
                                private JTextField txMath=new JTextField();
                                private JTextField txPhy=new JTextField();
                                private JButton btnComfirm=new JButton("确认");
                                private JPanel panel=new JPanel();
                                private JLabel lbName =new JLabel("姓名");
                                private JLabel lbNo =new JLabel("学号");
                                private JLabel lbTeacher =new JLabel("教师");
                                private JLabel lbCol =new JLabel("院系");
                                private JLabel lbEng =new JLabel("英语成绩");
                                private JLabel lbMath =new JLabel("高数成绩");
                                private JLabel lbPhy =new JLabel("大物成绩");

                                public AddStu(){
                                    super();
                                    this.setTitle("输入学生信息");
                                    this.setBounds(820, 330, 350, 300);
                                    this.setVisible(true);
                                    this.setResizable(false);
                                    this.setIconImage(icon.getImage());
                                    panel.setLayout(null);
                                    txName.setBounds(70,5,200,30);
                                    txNo.setBounds(70,35,200,30);
                                    txTeacher.setBounds(70,65,200,30);
                                    txCol.setBounds(70,95,200,30);
                                    txEng.setBounds(70,125,200,30);
                                    txMath.setBounds(70,155,200,30);
                                    txPhy.setBounds(70,185,200,30);
                                    btnComfirm.setBounds(130,220,70,30);

                                    lbName.setBounds(30,5,200,30);
                                    lbNo.setBounds(30,35,200,30);
                                    lbTeacher.setBounds(30,65,200,30);
                                    lbCol.setBounds(30,95,200,30);
                                    lbEng.setBounds(20,125,200,30);
                                    lbMath.setBounds(20,155,200,30);
                                    lbPhy.setBounds(20,185,200,30);
                                    panel.add(lbName);
                                    panel.add(txName);
                                    panel.add(lbNo);
                                    panel.add(txNo);
                                    panel.add(lbTeacher);
                                    panel.add(txTeacher);
                                    panel.add(lbCol);
                                    panel.add(txCol);
                                    panel.add(lbEng);
                                    panel.add(txEng);
                                    panel.add(lbMath);
                                    panel.add(txMath);
                                    panel.add(lbPhy);
                                    panel.add(txPhy);
                                    panel.add(btnComfirm);
                                    this.add(panel);
                                    btnComfirm.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String sname="";
                                            int sNo=Integer.parseInt(txNo.getText());
                                            String tname="";
                                            String col="";
                                            int eng=Integer.parseInt(txEng.getText());
                                            int math=Integer.parseInt(txMath.getText());
                                            int phy=Integer.parseInt(txPhy.getText());
                                            if (!("".equals(txName.getText()))){
                                                sname=txName.getText();
                                            }
                                            if (!("".equals(txTeacher.getText()))){
                                                tname=txTeacher.getText();
                                            } if (!("".equals(txCol.getText()))){
                                                col=txCol.getText();
                                            }
                                            String sql="insert into studentI(name,number," +
                                                    "teacher,major,english,math,physics) " +
                                                    "values (?,?,?,?,?,?,?);";
                                            String finalSname = sname;
                                            String finalTname = tname;
                                            String finalCol = col;
                                            new Thread(() -> {//新开线程添加数据
                                                PreparedStatement ps;
                                                try {
                                                    ps=conn.getConnect().prepareStatement(sql);
                                                    ps.setString(1, finalSname);
                                                    ps.setInt(2, sNo);
                                                    ps.setString(3, finalTname);
                                                    ps.setString(4, finalCol);
                                                    ps.setInt(5, eng);
                                                    ps.setInt(6, math);
                                                    ps.setInt(7, phy);
                                                    ps.executeUpdate();
                                                    table.validate();
                                                    table.updateUI();
                                                } catch (SQLException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }).start();

                                            dispose();

                                        }
                                    });
                                }
                            }
                            AddStu addStu=new AddStu();
                        }

                    });
                    findByName.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            class FindByName extends JFrame{
                                String input ="";
                                JTextField txInput=new JTextField();
                                JPanel panel=new JPanel();
                                JLabel lbInput =new JLabel("输入");
                                JButton btnComfirm=new JButton("确认");
                                public FindByName() {
                                    super();
                                    this.setTitle("请输入学号或姓名或老师或院系");
                                    this.setBounds(820, 330, 350, 200);
                                    this.setVisible(true);
                                    this.setResizable(false);
                                    this.setIconImage(icon.getImage());
                                    panel.setLayout(null);
                                    txInput.setBounds(70, 20, 200, 30);
                                    lbInput.setBounds(40, 20, 200, 30);
                                    btnComfirm.setBounds(120, 60, 70, 30);
                                    panel.add(lbInput);
                                    panel.add(txInput);
                                    panel.add(btnComfirm);
                                    this.add(panel);

                                    btnComfirm.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            input =txInput.getText();
                                            //使用正则表达式提取输入数字
                                            String regEx="[^0-9]";
                                            Pattern p = Pattern.compile(regEx);
                                            Matcher m = p.matcher(input);
                                            //为解决关键词查询String与number的冲突
                                            int  forNumber;
                                            if ("".equals(m.replaceAll("").trim())){
                                                forNumber=999999;
                                            }else forNumber=Integer.parseInt(m.replaceAll("").trim());
                                            dataModel.clear();
                                            try {
                                                Statement stmt = conn.getConnect().createStatement();
                                                ResultSet rs = stmt.executeQuery("select * from studentI where name like'%"+ input
                                                        +"%'or number like'%"+ forNumber
                                                        +"%'or teacher like '%"+input+"%'or major like '%"+input+"%';");
                                                initTable(rs);
                                            } catch (SQLException e1) {
                                                e1.printStackTrace();
                                            }
                                            dispose();
                                            table.validate();
                                            table.updateUI();
                                        }
                                    });
                                }
                            }
                            new Thread(() -> {
                                FindByName finder=new FindByName();
                            }).start();
                        }
                    });
                    showEng.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dataModel.clear();
                            try {
                                Statement stmt =conn.getConnect().createStatement();
                                ResultSet rs = stmt.executeQuery("select *from studentI order by english desc");
                                initTable(rs);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            //更新表格
                            table.validate();
                            table.updateUI();
                        }
                    });
                    showMath.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dataModel.clear();
                            try {
                                Statement stmt =conn.getConnect().createStatement();
                                ResultSet rs = stmt.executeQuery("select *from studentI order by math desc");
                                initTable(rs);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            //更新表格
                            table.validate();
                            table.updateUI();
                        }
                    });
                    showPhy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dataModel.clear();
                            try {
                                Statement  stmt = conn.getConnect().createStatement();
                                ResultSet rs =  stmt.executeQuery("select *from studentI order by physics desc ");
                                initTable(rs);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            //更新表格
                            table.validate();
                            table.updateUI();
                        }
                    });
                    userEx.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            setVisible(false);
                            EnterGui enterGui=new EnterGui();
                        }
                    });
                }
                int Max(int a,int b,int c){
                    if (a>=b&&a>=c)
                        return a;
                    if (b>=a&&b>=c)
                        return b;
                    if (c>=a&&c>=b)
                        return c;
                    else return 0;
                }
                int Min(int a,int b,int c){
                    if (a<=b&&a<=c)
                        return a;
                    if (b<=a&&b<=c)
                        return b;
                    if (c<=a&&c<=b)
                        return c;
                    else return 0;
                }
                //table赋值
                 private void initTable(ResultSet rs) throws SQLException {
                    Vector<String> record;
                    while (rs.next()) {
                        record = new Vector<String>();
                        for (int i = 0; i < COLUMN; i++) {
                            if(i<7) {
                                record.add(rs.getString(i + 1));
                            }else if (i==7){//平均成绩
                                record.add(Integer.toString(((rs.getInt(7)+rs.getInt(5)+rs.getInt(6))/3)));
                            }else if (i==8){
                                record.add(Integer.toString(Max(rs.getInt(5),rs.getInt(6),rs.getInt(7))));
                            }else if (i==9){
                                record.add(Integer.toString(Min(rs.getInt(5),rs.getInt(6),rs.getInt(7))));
                            }
                        }
                        dataModel.add(record);
                    }
                }
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                Statement statement;
                ResultSet resultSet;
            String username = "";
            String password = "";
            username = userName.getText();
            password = String.valueOf(pass.getPassword()).trim();
            String sql = "select password from usersI where username='" + username + "';";
                try {
                statement = conn.getConnect().createStatement();
                resultSet = statement.executeQuery(sql);
                System.out.println("执行查询");
                if (resultSet.next()) {
                    //用户名对应密码相等则进入主界面
                    if (resultSet.getString(1).equals(password)) {
                        dispose();
                        MainGUI mainGUI = new MainGUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误",
                                "提示", JOptionPane.INFORMATION_MESSAGE);
                    }
                }else JOptionPane.showMessageDialog(null, "用户名或密码错误",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            }
        });
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                class Register extends JFrame {
                    private JTextField txName=new JTextField();
                    private JTextField txPass=new JTextField();
                    private JButton btnComfirm=new JButton("确认");
                    private JLabel lbInputName =new JLabel("用户名");
                    private JLabel lbInputPass =new JLabel("密码");
                    private JPanel panel=new JPanel();
                    private ConnetDB conn=new ConnetDB();
                    public Register(){
                        super();
                        this.setTitle("注册");
                        this.setBounds(720, 320, 350, 200);
                        this.setVisible(true);
                        this.setResizable(false);
                        panel.setLayout(null);
                        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                        txName.setBounds(70, 20, 200, 30);
                        txPass.setBounds(70, 60, 200, 30);
                        lbInputName.setBounds(30, 20, 40, 30);
                        lbInputPass.setBounds(40, 60, 40, 30);
                        btnComfirm.setBounds(120, 100, 70, 30);
                        this.add(panel);
                        panel.add(lbInputName);
                        panel.add(txName);
                        panel.add(lbInputPass);
                        panel.add(txPass);
                        panel.add(btnComfirm);
                        panel.updateUI();
                        btnComfirm.addActionListener(new ActionListener() {
                            String username="";
                            String password="";
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (!("".equals(txName.getText()))){
                                    username=txName.getText();
                                }
                                if (!("".equals(txPass.getText()))){
                                    password=txPass.getText();
                                }
                                String sql="insert into usersI values(?,?);";
                                PreparedStatement ps;
                                try {
                                    ps = conn.getConnect().prepareStatement(sql);
                                    ps.setString(1, username);
                                    ps.setString(2, password);
                                    ps.executeUpdate();
                                    JOptionPane.showMessageDialog(null, "注册成功",
                                "提示", JOptionPane.INFORMATION_MESSAGE);

                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                              dispose();
                            }
                        });
                    }
                }
                new Thread(new Runnable() {//避免窗口卡死
                    @Override
                    public void run() {
                        Register register =new Register();
                    }
                }).start();
            }
        });
    }
    class ConnetDB {
        public Connection getConnect(){
            Connection connect = null;
            //登陆数据库的用户
            String username="root";
            String password="lyh690042939";
            final String URL="jdbc:mysql://localhost:3306/studentDB?useSSL=false&serverTimezone=UTC";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connect =DriverManager.getConnection(URL,username,password);
                System.out.println("连接成功");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connect;
        }
    }
}