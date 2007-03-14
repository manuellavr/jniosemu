package jniosemu.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.*;

import jniosemu.events.*;
import jniosemu.emulator.*;

/**
 * Creates and manages the GUI component of the memory view.
 */
 public class GUIMemoryView extends JFrame
                              implements ActionListener, EventObserver {

	/**
	 * Reference to EventManager used to receive
	 * and send events.
	 */
	private EventManager eventManager;

	private JList memoryList;

	/**
	 * Initiates the creation of GUI components and adds itself to
	 * the Event Manager as an observer.
	 *
	 * @post      eventManager reference is set for this object.
	 * @calledby  GUIManager.setup()
	 * @calls     setup(), EventManager.addEventObserver()
	 *
	 * @param  eventManager  The Event Manager object.
	 */
	public GUIMemoryView(EventManager eventManager)
	{
		super("Memory View");

		this.eventManager = eventManager;

		setup();

		// add events to listen to
		this.eventManager.addEventObserver(Events.EVENTID_PC_CHANGE, this);
		this.eventManager.addEventObserver(Events.EVENTID_RESET, this);
	}

	/**
	 * Setup GUI components and attributes.
	 *
	 * @post      components created and added to panel
	 * @calledby  GUIMemoryView
	 */
	private void setup()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// list
		JPanel listPanel = new JPanel(new GridLayout(1,1));
		
		listPanel.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(4, 4, 0, 4),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)
			)
		);		
		
		Vector<String> tmp = new Vector<String>();
		tmp.add("minne");
		tmp.add("grej");
		tmp.add("sak");
		
		memoryList = new JList(tmp);
		listPanel.add(memoryList);

    // button
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    JButton button = new JButton("Close");
    button.addActionListener(this);
    buttonPanel.add(button);

    // container
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    
    contentPane.add(listPanel, BorderLayout.CENTER);
    contentPane.add(buttonPanel, BorderLayout.PAGE_END);
    
	}

	public void update(String eventIdentifier, Object obj)
	{
		if (eventIdentifier.equals(Events.EVENTID_PC_CHANGE))
		{
			//
		}
	}

	/**
	 * Invoked when a GUI action occurs..
	 *
	 * @param  e  action event object
	 */
	public void actionPerformed(ActionEvent e) {
	  setVisible(false);
	  dispose();
	}

}