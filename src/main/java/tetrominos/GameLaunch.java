package tetrominos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameLaunch extends JFrame {
    public static final int H_FRAME = 360;
	public static final int W_FRAME = 540;
    private Insets insets;
    private JPanel contentPane;
    public Board board;

    public GameLaunch() {
        super("Tetrominos!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(W_FRAME, H_FRAME));
        setLocationRelativeTo(null); //Center the frame
        setLocation(getX() - 80, getY() - 80);
        setLayout(null);
        pack();
		setResizable(false);
		setVisible(true);

        insets = this.getInsets();
        getGameSetUpFromUsers();
        
    }

    public void getGameSetUpFromUsers() {
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBounds(insets.left, insets.top, W_FRAME - insets.left - insets.right, 
				H_FRAME - insets.bottom - insets.top);

		JLabel rowsLabel = new JLabel("How many rows would you like our board to have?:", JLabel.CENTER);
        rowsLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rowsLabel.setBounds(20,140,400,20);
		
        JTextField rowsText = new JTextField(2);
        rowsText.setName("boardRows");
        rowsText.setBounds(rowsLabel.getX() + rowsLabel.getWidth() + 10, 
				rowsLabel.getY(), 20, rowsLabel.getHeight());
        contentPane.add(rowsLabel);
		contentPane.add(rowsText);

		JLabel columnsLabel = new JLabel("How many columns would you like our board to have?:", JLabel.CENTER);
        columnsLabel.setFont(rowsLabel.getFont());
		columnsLabel.setBounds(rowsLabel.getX(), rowsLabel.getY() + 40, 
				rowsLabel.getWidth(), rowsLabel.getHeight());
		JTextField columnsText = new JTextField(2);
        columnsText.setName("boardCols");
        columnsText.setBounds(columnsLabel.getX() + columnsLabel.getWidth() + 10, 
				columnsLabel.getY(), 20, columnsLabel.getHeight());
        contentPane.add(columnsLabel);
        contentPane.add(columnsText);

        JButton startGameButton = new JButton("Let's Start!");
        startGameButton.setName("startButton");
        startGameButton.setBounds(rowsLabel.getX() + 140, rowsLabel.getY() + 120, 200, 22);
		startGameButton.setFocusPainted(false);
		contentPane.add(startGameButton);

		startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rowsInput = rowsText.getText();
                System.out.format("Rows to have: %s \t",rowsInput);
                String columnsInput = columnsText.getText();
                System.out.format("Columns to have: %s",columnsInput);
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        GameLaunch.this.dispose();
                        GameLaunch.this.board = new Board(Integer.valueOf(columnsInput),Integer.valueOf(rowsInput));
                    }
                });
            }
        });

        setContentPane(contentPane);
    }
}
