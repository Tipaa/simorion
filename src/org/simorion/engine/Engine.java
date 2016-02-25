package org.simorion.engine;

import org.simorion.common.Song;
import org.simorion.common.stream.SongFormat;
import org.simorion.common.stream.SongReader;
import org.simorion.common.stream.SongWriter;
import org.simorion.ui.model.MutableModel;

public interface Engine extends MutableModel {

	
	//TODO
	//public void attach(final SoundSystem s);
	
	//TODO: consider
	//public void load(final Song s);
	
	public Song getSong();

	void receiveFromStream(SongReader stream, SongFormat f);
	
	void sendToStream(SongWriter stream, SongFormat f);
	
}