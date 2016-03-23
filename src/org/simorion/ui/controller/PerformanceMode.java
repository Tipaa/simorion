package org.simorion.ui.controller;

import java.awt.event.MouseEvent;

import org.simorion.common.util.Util;
import org.simorion.sound.SingleSound;
import org.simorion.sound.SoundSystem;
import org.simorion.ui.view.AnimationView;
import org.simorion.ui.view.DefaultView;
import org.simorion.ui.view.View;
 
/**
 * Device Mode implementation for the Performance Mode.
 * @author Karl Brown
 *
 */
public class PerformanceMode extends DeviceMode {
	
	SoundSystem soundSystem = SoundSystem.getInstance();
	boolean isFresh = false;
 
    public PerformanceMode(ModeMaster m) {
		super(m);
	}
    
	private PerformanceView instance = new PerformanceView();
	
    public View getView() {
    	//if(isFresh) return new AnimationView();
        return instance;
    }
     
    /**
     * Implementation of the View interface for the PerformanceView
     * @author Karl Brown
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
    		int loop = model.getCurrentLayer().getLoopPoint();
    		loop = loop == 0?16:loop;
    		return model.getCurrentLayer().getRow(y).isLit(x) || (model.getTick() % loop == x && (y == 0 || y == 5 || y == 10 || y == 15));
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
    	 * Retrieves and returns the current textual output of the LCD.
    	 * @return The textual output of the LCD.
    	 */
    	@Override
    	public String getLCDMessage() {
    		return model.getCurrentLayer().getVoice().getName();
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

    }
          
    @Override
    public void onOnOffButtonPress(MouseEvent e) {
    	if(e.getClickCount() == 2) System.exit(0);
    	model.stopPlaying();
    	model.reset();
        changeMode(ModeMaster.ON_OFF_MODE);
    }
     
	/**
	 * In Performance Mode, changes the current mode to the mode represented by the button.
	 */
    @Override
    public void onLButtonPress(MouseEvent e, int ButtonNum){
        isFresh = false;  
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
     
	/**
	 * In Performance Mode, changes the current mode to the mode represented by the button.
	 */
    @Override
    public void onRButtonPress(MouseEvent e, int ButtonNum){
        isFresh = false;  
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOKButtonPress(MouseEvent e) {
		if (isFresh) {
			changeMode(ModeMaster.SHOP_BOY_MODE);
		} else {
			model.setLCDDisplay("Layer " + model.getCurrentLayerId() + " | " + model.getCurrentLayer().getVoice().getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMatrixButtonPress(MouseEvent e, int x, int y) {
		isFresh = false;
		//TODO: testing code while we figure out demos
		model.enqueueSound(new SingleSound(
				model.getCurrentLayer().getVoice().getMidiVoice(),
				model.getCurrentLayer().getRow(y).getNote(),
				1,
				model.getCurrentLayer().getVelocity()));
		
		model.getCurrentLayer().getRow(y).toggleLit(x);
		soundSystem.updateSequence(model.getCurrentLayerId(), x, y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMatrixButtonPress(MouseEvent e, int x, int y, boolean lit) {
		isFresh = false;
		if (lit) model.getCurrentLayer().getRow(y).setLit(x);
		else model.getCurrentLayer().getRow(y).setUnlit(x);
		soundSystem.updateSequence(model.getCurrentLayerId(), x, y);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	void onChangedTo() {
		model.startPlaying();
		model.setLCDDisplay("Layer " + model.getCurrentLayerId() + " | " + model.getCurrentLayer().getVoice().getName());
	}
}