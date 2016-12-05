

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Ui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	boolean whatClicked = false;
	boolean saved = true;
	boolean ifOpened = false;
	final Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);
	final Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private JLabel text, map;
	private JTextField searchField, categoryName, describedName;
	private JButton searchButton, hidePlace, deletePlace, whatIsHere,
			hideCategory, newCategory, deleteCategory, categoryOk,
			categoryCancel;
	private JTextArea describedInfo;
	@SuppressWarnings("rawtypes")
	private JList categoryBox;
	private JDialog categoryPopUp;
	final static JFrame browser = new JFrame();
	BufferedImage myPicture;
	private JColorChooser colorChoice;
	private JComboBox<String> dropdown;
	String absolutPath;
	DefaultListModel<Category> categoryList = new DefaultListModel<Category>();
	HashMap<Position, Triangel> triangelPositionHashMap = new HashMap<Position, Triangel>();

	Ui() {
		menu();
		setVisible(true);
		setTitle("Platser");
		add(initNorthPanel(), BorderLayout.NORTH);
		add(initEastPanel(), BorderLayout.EAST);
		
		describedInfo = new JTextArea(5, 0);
		describedName = new JTextField();
		map = new JLabel();
		categoryPopUp = new JDialog();
		categoryName = new JTextField();
		categoryOk = new JButton("Ok");
		categoryCancel = new JButton("Cancel");
		newCategoryPopUp();
		add(map);
		dropdown.setEnabled(false);
		dropdown.setSelectedIndex(-1);
		pack();
		
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
				     notSavedExit();
		    }
		});
	}

	private void menu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu archive = new JMenu("Archive");
		JMenuItem newMap = new JMenuItem("New map");
		newMap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!saved) {
					notSavedNewMap();
				} else {
					newPop(browser);
					
				}
			}
		});

		JMenuItem open = new JMenuItem("Open");
		browser.getContentPane().setLayout(new FlowLayout());
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!saved) {
					notSavedOpenMap();
				} else {
					openPop(browser);
				}
			}
		});

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				savePop(browser);
			}
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				notSavedExit();
			}
		});
		archive.add(newMap);
		archive.add(open);
		archive.add(save);
		archive.add(exit);
		menuBar.add(archive);
		setJMenuBar(menuBar);
	}

	@SuppressWarnings("unchecked")
	private void openPop(final JFrame frame) {
		 JFileChooser openFile = new JFileChooser();
		 FileFilter filter = new FileNameExtensionFilter("Image files", "krt");
		 openFile.setFileFilter(filter);
		 int open = openFile.showOpenDialog(frame);
		 if (open == JFileChooser.APPROVE_OPTION) {
			 clearAll();
			 saved = true;
			 File file = openFile.getSelectedFile();
			 absolutPath = file.getAbsolutePath();
			 ImageIcon image = null;
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				image = (ImageIcon)objectInputStream.readObject();
				triangelPositionHashMap = (HashMap<Position, Triangel>)objectInputStream.readObject();
				categoryList = (DefaultListModel<Category>)objectInputStream.readObject();
				objectInputStream.close();
				fileInputStream.close();
			}catch(Exception e){
				
			}
		map.setIcon(image);
		categoryBox.setModel(categoryList);
		updateMarkers();
		setMouseListenerForMap();
		pack();
		ifOpened = true;
		dropdown.setEnabled(true);
		 }
	}

	private void savePop(final JFrame frame) {
		if (ifOpened) {
		        try {
		    		FileOutputStream fileOutputStream = new FileOutputStream(absolutPath);
		    		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		    		objectOutputStream.writeObject(map.getIcon());
		    		objectOutputStream.writeObject(triangelPositionHashMap);
		    		objectOutputStream.writeObject(categoryList);
		    		objectOutputStream.close();
		    		fileOutputStream.close();
				} catch (Exception e) {
					
				}
		        saved = true;
		} else {
		    JFileChooser fileSave = new JFileChooser();
		    int retrival = fileSave.showSaveDialog(null);
		    if (retrival == JFileChooser.APPROVE_OPTION) {
		        File f = fileSave.getSelectedFile();
		        String test = f.getAbsolutePath() + ".krt";
		        try {
		    		FileOutputStream fileOutputStream = new FileOutputStream(test);
		    		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		    		objectOutputStream.writeObject(map.getIcon());
		    		objectOutputStream.writeObject(triangelPositionHashMap);
		    		objectOutputStream.writeObject(categoryList);
		    		objectOutputStream.close();
		    		fileOutputStream.close();
				} catch (Exception e) {
					
				}
		        saved = true;
		    }
		}
	}

	private void newPop(final JFrame frame) {
		 JFileChooser newFile = new JFileChooser();
		 FileFilter filter = new FileNameExtensionFilter("Image files", "png",
		 "jpg", "krt", "jpeg", "tif");
		 newFile.setFileFilter(filter);
		 int newOpen = newFile.showOpenDialog(frame);
		 if (newOpen == JFileChooser.APPROVE_OPTION) {
			clearAll();
			saved = true;
			File file = newFile.getSelectedFile();
			try {
				myPicture = ImageIO.read(file);
			} catch (IOException e) {
				
			}
		map.setIcon(new ImageIcon(myPicture));
		pack();
		setMouseListenerForMap();
		ifOpened = false;
		dropdown.setEnabled(true);
		 }
	}

	private void setMouseListenerForMap() {
		if (map.getMouseListeners().length == 0) {
			map.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (whatClicked) {
						whatClicked = false;
						checkNearMarker(e);
					} else {
						if (dropdown.getSelectedIndex() == 0) {
							doDescribedPlacePopup(e);
						} else if (dropdown.getSelectedIndex() == 1) {
							doNamedPlacePopup(e);
						}
					}
				}
			});
		}
	}

	protected void checkNearMarker(MouseEvent me) {
		dropdown.setSelectedIndex(-1); 
		int x = me.getX();
         int y = me.getY();
         for (int i = (x-30); i < (x+30); i++) {
                 for (int k = (y-30); k < (y+30); k++) {
                         Position position = new Position(i, k);
                         if(triangelPositionHashMap.get(position) != null) {
                                 Triangel t = triangelPositionHashMap.get(position);
                                 
                                 t.setVisible(true);
                         }
                 }
         }
		
	}

	private void newCategoryPopUp() {
		JPanel north2 = new JPanel();
		JPanel south = new JPanel();
		categoryPopUp.setLayout(new BorderLayout());
		categoryPopUp.setSize(500, 500);
		north2.add(new JLabel("Name: "));
		categoryName.setPreferredSize(new Dimension(150, 27));
		north2.add(categoryName);
		categoryPopUp.add(north2, BorderLayout.NORTH);
		colorChoice = new JColorChooser(categoryPopUp.getForeground());
		colorChoice.setSelectionModel(new DefaultColorSelectionModel());
		colorChoice.getSelectionModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
			}
		});
		;
		categoryPopUp.add(colorChoice);
		categoryPopUp.add(south, BorderLayout.SOUTH);
		south.add(categoryOk);
		categoryOk.addActionListener(this);
		south.add(categoryCancel);

		categoryCancel.addActionListener(this);

		categoryPopUp.setVisible(false);

	}

	private void updateCat(Category category) {
		categoryList.addElement(category);
		categoryPopUp.setVisible(false);
		categoryName.setText("");
		saved = false;

	}

	private void deleteCat() {
		if (!categoryBox.isSelectionEmpty()) {
			categoryList.remove(categoryBox.getSelectedIndex());
			saved = false;
		}
	}

	private void markerSettings(Triangel marker) {
		map.add(marker);
		marker.setOpaque(false);
		marker.setSize(getSize());
		dropdown.setSelectedIndex(-1);
		categoryBox.clearSelection();
		pack();
		saved = false;
	}
	
	private void updateMarkers() {
		for (Triangel updateMarker : triangelPositionHashMap.values()) {
			map.add(updateMarker);
			setMouseListenerForMarker(updateMarker);
		}
		pack();
	}

	private JPanel initNorthPanel() {
		JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));

		searchField = new JTextField();
		searchButton = new JButton("Search");
		hidePlace = new JButton("Hide places");
		deletePlace = new JButton("Delete places");
		whatIsHere = new JButton("What is here?");
		text = new JLabel("New:");
		dropdown = new JComboBox<String>();
		dropdown.addItem("DescribedPlace");
		dropdown.addItem("NamedPlace");

		north.add(text);
		north.add(dropdown);
		north.add(searchField);
		north.add(searchButton);
		north.add(hidePlace);
		north.add(deletePlace);
		north.add(whatIsHere);
		searchField.setPreferredSize(new Dimension(150, 27));
		searchField.setText("Search");
		whatIsHere.addActionListener(this);
		hidePlace.addActionListener(this);
		deletePlace.addActionListener(this);
		dropdown.addActionListener(this);
		searchButton.addActionListener(this);

		return north;
	}

	private JPanel initEastPanel() {
		JPanel east = new JPanel(new GridBagLayout());

		categoryBox = new JList<Category>(categoryList);
		hideCategory = new JButton("Hide category");
		deleteCategory = new JButton("Delete category");
		newCategory = new JButton("New category");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 2;
		east.add(new JLabel("Categories "), gbc);
		gbc.gridy = 3;
		east.add(categoryBox, gbc);
		gbc.gridy = 4;
		east.add(hideCategory, gbc);
		gbc.gridy = 5;
		east.add(newCategory, gbc);
		gbc.gridy = 6;
		east.add(deleteCategory, gbc);
		gbc.weightx = gbc.weighty = 40;
		gbc.gridy = 1;
		east.add(new JLabel(""), gbc);

		categoryBox.setPreferredSize(new Dimension(150, 250));
		categoryBox.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		categoryBox.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
					for (Triangel marked : triangelPositionHashMap.values()) {
						if (marked != null && marked.isMarked()) {
						marked.setMarked(false);
						}
				}
					if (!categoryBox.isSelectionEmpty()) {
					for (Triangel marker : categoryList.get(categoryBox.getSelectedIndex()).getTriangel()) {
						marker.setMarked(true);
						marker.setVisible(true);
					}
				}
			}
		});

		hideCategory.addActionListener(this);
		newCategory.addActionListener(this);
		deleteCategory.addActionListener(this);
		return east;
	}

	private JPanel initPopupDescribed() {
		JPanel popUpDescribed = new JPanel();
		popUpDescribed.setLayout(new BoxLayout(popUpDescribed, BoxLayout.Y_AXIS));
		popUpDescribed.add(new JLabel("Name"));
		popUpDescribed.add(describedName);
		popUpDescribed.add(new JLabel("Information"));
		popUpDescribed.add(new JScrollPane(describedInfo));

		return popUpDescribed;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == deleteCategory) {
			removeMarker();
			deleteCat();
			for (Triangel marked : triangelPositionHashMap.values()) {
				if (marked.isMarked()) {
					map.remove(marked);
					map.repaint();
					map.validate();
				}
			}

		} else if (e.getSource() == newCategory) {
			categoryPopUp.setVisible(true);
		} else if (e.getSource() == dropdown) {
			if (dropdown.getSelectedIndex() < 0) {
				map.setCursor(normalCursor);
			} else {
				map.setCursor(crosshair);
			}
		} else if (e.getSource() == hidePlace) {
			hideMarker();
		} else if (e.getSource() == deletePlace) {
			removeMarker();
		} else if (e.getSource() == hideCategory) {
			hideMarkerCategory();
		} else if (e.getSource() == categoryOk) {
			if (categoryName.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "You must enter name");
			} else {
				updateCat(new Category(categoryName.getText(),colorChoice.getColor()));
			}
		} else if (e.getSource() == whatIsHere) {	
			whatClicked = true;
		} else if (e.getSource() == categoryCancel) {
			categoryPopUp.setVisible(false);
			categoryName.setText("");
		} else if (e.getSource() == searchButton) {
			methodSearch();
		} 
	}

	private void setMouseListenerForMarker(Triangel marker) {
		if (marker.getMouseListeners().length == 0) {
			marker.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent mev) {
					super.mouseClicked(mev);
					Triangel folded = (Triangel) mev.getSource();
					if (mev.getButton() == MouseEvent.BUTTON3) {
						folded.setFolded(!folded.isFolded());
					} else if (mev.getButton() == MouseEvent.BUTTON1) {
						folded.setMarked(!folded.isMarked());
					}
				}
			});
		}
	}
	
	private void hideMarkerCategory() {
		if (!categoryBox.isSelectionEmpty()) {
			for (Triangel marked : categoryList.get(categoryBox.getSelectedIndex()).getTriangel()) {
				marked.setVisible(false);
				marked.setMarked(false);
				saved = false;
			}
		}
	}
	
	private void hideMarker() {
			for (Triangel marked : triangelPositionHashMap.values()) {
					if (marked != null && marked.isMarked()) {
					marked.setVisible(false);
					marked.setMarked(false);
					saved = false;
					}
			}
	}

	
	private void clearAll() {
		for(Iterator<Map.Entry<Position, Triangel>> hashmap = triangelPositionHashMap.entrySet().iterator(); hashmap.hasNext(); ) {
			Entry<Position, Triangel> entry = hashmap.next();
			hashmap.remove();
			map.remove(entry.getValue());
			map.repaint();
			map.validate();
			saved = false;
		}
		triangelPositionHashMap.clear();
		categoryList.removeAllElements();
		repaint();
		validate();
	}
	
	private void removeMarker() {		
		for(Iterator<Map.Entry<Position, Triangel>> hashmap = triangelPositionHashMap.entrySet().iterator(); hashmap.hasNext(); ) {
		      Entry<Position, Triangel> entry = hashmap.next();
			if (entry.getValue() != null && entry.getValue().isMarked()){
				hashmap.remove();
				map.remove(entry.getValue());
				map.repaint();
				map.validate();
				saved = false;
			}
		}
	}

	private void updateColorForMarker(Triangel marker) {
		if (categoryBox.getSelectedIndex() <= -1) {
			marker.setColor(Color.BLACK);
		} else {
			marker.setColor(categoryList.getElementAt(categoryBox.getSelectedIndex()).getColor());
		}
	}

	private void doDescribedPlacePopup(MouseEvent mev) {
		int result = JOptionPane.showConfirmDialog(null, initPopupDescribed(),
				"DescribedPlace",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION
				&& (!describedName.getText().equalsIgnoreCase("") 
				|| !describedInfo.getText().equalsIgnoreCase(""))) {
			DescribedPlace describedPlaceInput = new DescribedPlace(describedName.getText(), describedInfo.getText());
			Triangel marker = new Triangel(describedPlaceInput);
			marker.setLocation(mev.getX() - 10, mev.getY() - 20);
			setMouseListenerForMarker(marker);
				triangelPositionHashMap.put(new Position(mev.getX(), mev.getY()), marker);
			if (categoryBox.getSelectedIndex() >= 0) {
				categoryList.getElementAt(categoryBox.getSelectedIndex()).getTriangel().add(marker);
			}
			describedName.setText("");
			describedInfo.setText("");
			updateColorForMarker(marker);
			markerSettings(marker);
		}
	}

	private void doNamedPlacePopup(MouseEvent mev) {
		String namedPlace = JOptionPane.showInputDialog("Please input a value"); // namedPLace
		if (!(namedPlace == null) && !namedPlace.equalsIgnoreCase("")) {
			Place namedPlaceInput = new Place(namedPlace);
			Triangel marker = new Triangel(namedPlaceInput);
			marker.setLocation(mev.getX() - 10, mev.getY() - 20);
			setMouseListenerForMarker(marker);
			triangelPositionHashMap.put(new Position(mev.getX(), mev.getY()), marker);
			if (categoryBox.getSelectedIndex() >= 0) {
				categoryList.getElementAt(categoryBox.getSelectedIndex()).getTriangel().add(marker);
			}
			updateColorForMarker(marker);
			markerSettings(marker);
		}
	}
	
	private void notSavedExit() {
		if (!saved) {
			int notSaved = JOptionPane.showConfirmDialog(null, "File not saved. If you want to save press cancel.", "Save file?", JOptionPane.OK_CANCEL_OPTION);
			if (notSaved == JOptionPane.OK_OPTION) {
				System.exit(0);
			} else {
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			}
		} else {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			System.exit(0);
		}
	}

	private void notSavedNewMap() {
		if (!saved) {
			int notSaved = JOptionPane.showConfirmDialog(null, "File not saved. If you want to save press cancel.", "Save file?", JOptionPane.OK_CANCEL_OPTION);
			if (notSaved == JOptionPane.OK_OPTION) {
				newPop(browser);
			}
		}
	}
	
	private void notSavedOpenMap() {
		if (!saved) {
			int notSaved = JOptionPane.showConfirmDialog(null, "File not saved. If you want to save press cancel.", "Save file?", JOptionPane.OK_CANCEL_OPTION);
			if (notSaved == JOptionPane.OK_OPTION) {
				openPop(browser);
			}
		}
	}

	private void methodSearch() {
		for (Triangel marked : triangelPositionHashMap.values()) {
			if (marked != null && marked.isMarked()) {
			marked.setMarked(false);
			}
		}
		hideMarker();
		for (Triangel search : triangelPositionHashMap.values()) {
				if (searchField.getText().equalsIgnoreCase(search.getPlace().getName())) {
					search.setVisible(true);
					search.setMarked(true);
				}
		}
	}
}