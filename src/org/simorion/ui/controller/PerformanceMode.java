package org.simorion.ui.controller;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.simorion.common.ImmutableRow;
import org.simorion.common.util.Util;
import org.simorion.ui.view.DefaultView;
import org.simorion.ui.view.View;
 
public class PerformanceMode extends DeviceMode {
 
    public PerformanceMode(ModeMaster m) {
		super(m);
	}
    
	private PerformanceView instance = new PerformanceView();
     
    /**
     * Implementation of the View interface for the PerformanceView
     * @author Karl Brown, Petar Krstic
     *
     */
    private class PerformanceView extends DefaultView {
    	
    	/**
    	 * Retrieves and returns whether or not the button at coordinate {@code x}, {@code y} is lit.
    	 * @param x The x coordinate of the button.
    	 * @param y The y coordinate of the button.
    	 * @return True if lit, false otherwise.
    	 */
    	@Override
    	public boolean isLit(int x, int y) {
    		return model.getCurrentLayer().getRow(y).isLit(x);
    	}

    	/**
    	 * Determines if the row of buttons {@code row} is on are lit. 
    	 * @param y The row to check.
    	 * @return True if all buttons are lit, false otherwise.
    	 */
    	@Override
    	public boolean isRowLit(int x) {
    		// Each row should contain the same number of buttons.
    		int columns = model.getCurrentLayer().getRow(0).cellCount();
    		
    		// Keeping the row constant, check the state of each button.
    		for (int i = 0; i < columns; i++) {
    			if (!isLit(x, i)) return false;
    		}
    		
    		// Not all the buttons on this row are lit.
    		return true;
    	}

    	/**
    	 * Determines if the column of buttons {@code column} is on are lit. 
    	 * @param x The column to check.
    	 * @return True if all buttons are lit, false otherwise.
    	 */
    	@Override
    	public boolean isColumnLit(int y) {			
    		// Each row should contain the same number of buttons.
    		int rows = Util.count(model.getCurrentLayer().getRows());
    		
    		// Keeping the row constant, check the state of each button.
    		for (int i = 0; i < rows; i++) {
    			if (!isLit(i, y)) return false;
    		}
    		
    		// Not all the buttons on this row are lit.
    		return true;
    	}
    	
    	/**
    	 * Retrieves and returns a collection of iterable booleans representing all the currently lit matrix buttons.
    	 * @return A collection of iterable booleans representing the currently lit buttons.
    	 */
    	@Override
    	public Collection<Iterable<Boolean>> getLitButtons() {
    		
    		List<Iterable<Boolean>> lit = new ArrayList<Iterable<Boolean>>();
    		
    		for (ImmutableRow r : model.getCurrentLayer().getRows()) {				
    			lit.add(Util.bitstring(r.getLit()));
    		}
    		
    		return lit;
    		
    	}

    	/**
    	 * Retrieves and returns the current textual output of the LCD.
    	 * @return The textual output of the LCD.
    	 */
    	@Override
    	public String getLCDMessage() {
    		return model.getCurrentLayer().getLCDMessage();
    	}

    	/**
    	 * Retrieves and returns the MIDI ID of the currently applied instrument.
    	 * @return The MIDI ID of the currently applied instrument for this view.
    	 */
    	@Override
    	public int getVoiceId() {
    		return model.getCurrentLayer().getVoice().getMidiVoice();
    	}

    	/**
    	 * Retrieves and returns the name of the currently applied instrument.
    	 * @return The name of the currently applied instrument for this view
    	 */
    	@Override
    	public String getVoiceName() {
    		return model.getCurrentLayer().getVoice().getName();
    	}

    	/**
    	 * Retrieves and returns the ID of the currently applied layer.
    	 * @return The ID of the currently applied layer for this view.
    	 */
    	@Override
    	public int getCurrentLayerId() {
    		return model.getCurrentLayer().getLayerNumber();
    	}

    	/**
    	 * Retrieves and returns the current loop point.
    	 * @return The current loop point for this view.
    	 */
    	@Override
    	public int getLoopPoint() {
    		return model.getCurrentLayer().getLoopPoint();
    	}

    	/**
    	 * Retrieves and returns the current velocity for notes played.
    	 * @return The current velocity for notes played on this view.
    	 */
    	@Override
    	public int getVelocity() {
    		return model.getCurrentLayer().getVelocity();
    	}
    	
    	/**
    	 * Retrieves and returns the current note for row {@code y}.
    	 * @param y The row to check.
    	 * @return The current note for row {@code y}
    	 */
    	@Override
    	public byte getNote(int y) {
    		return model.getCurrentLayer().getRow(y).getNote();
    	}

		@Override
		public void setLit(int x, int y) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setLCDMessage() {
			// TODO Auto-generated method stub
			
		} 

    }
     
    public View getView() {
        return instance;
    }
     
    @Override
    public void onOnOffButtonPress(MouseEvent e) {
    	// FIXME: This should be handled by the controller and not deferred to the view.
        //instance.clearButtons();
        changeMode(ModeMaster.ON_OFF_MODE);
    }
     
    public void onLButtonPressed(MouseEvent e, int ButtonNum){
          
        switch (ButtonNum){
            case 1 : 
                changeMode(ModeMaster.CHANGE_VOICE_MODE);
                break;
            case 2 : 
                changeMode(ModeMaster.CHANGE_VELOCITY_MODE);
                break;
            case 3 : 
                changeMode(ModeMaster.CHANGE_LOOP_SPEED_MODE);
                break;
            case 4 : 
                changeMode(ModeMaster.CHANGE_LOOP_POINT_MODE);
                break;
        }
    }
     
    public void onRButtonPressed(MouseEvent e, int ButtonNum){
          
        switch (ButtonNum){
            case 1 : 
                changeMode(ModeMaster.CHANGE_LAYER_MODE);
                break;
            case 2 : 
                changeMode(ModeMaster.SAVE_CONFIG_MODE);
                break;
            case 3 : 
                changeMode(ModeMaster.LOAD_CONFIG_MODE);
                break;
            case 4 : 
                changeMode(ModeMaster.MASTER_SLAVE_MODE);
                break;
        }
    }
     
    public void onMatrixPressed(MouseEvent e, int x, int y){
        //send button pressed to model
        if (model.getCurrentLayer().getRow(y).isLit(x)){
        	// FIXME: This should be handled by the controller and not deferred to the view.
            //instance.lightButton(x, y, Color.ORANGE);
        }else{
        	// FIXME: This should be handled by the controller and not deferred to the view.
            //instance.lightButton(x, y, Color.WHITE);
        }
    }

	@Override
	public void onOKButtonPress(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMatrixButtonPress(MouseEvent e, int buttonColumn, int buttonRow) {
		// TODO Auto-generated method stub
		
	}
}