package com.matthewtamlin.mixtape.example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.matthewtamlin.android_utilities.library.helpers.BitmapEfficiencyHelper;
import com.matthewtamlin.mixtape.library.data.LibraryReadException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import java.io.File;
import java.io.IOException;

/**
 * A utility class for reading data from ID3 files.
 */
public abstract class Id3Util {
	/**
	 * Reads the metadata stored in the ID3 tag of an MP3 file.
	 *
	 * @param file
	 * 		the MP3 file to read from
	 * @param field
	 * 		the metadata field to read
	 * @return the metadata, null if none is found
	 * @throws IllegalArgumentException
	 * 		if an error occurs while accessing the ID3 tag
	 */
	public static String getMetadataFromId3Tag(final File file, final MetadataField field)
			throws IOException {
		try {
			final Tag tag = AudioFileIO.read(file).getTag();
			return tag == null ? null : tag.getFirst(field.fieldKey);
		} catch (final Exception e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + field, e);
		}
	}

	/**
	 * Reads the cover art stored in the ID3v2 tag of an MP3 file. The supplied dimensions are used
	 * to optimise the image so that memory usage is reduced without distortion or degradation. If
	 * either dimension parameter is less than or equal to 0, then the full unoptimised cover art is
	 * returned.
	 *
	 * @param file
	 * 		the MP3 file to read from
	 * @param width
	 * 		the desired width of the cover art
	 * @param height
	 * 		the desired height of the cover art
	 * @return the cover art, null if none is found
	 * @throws IllegalArgumentException
	 * 		if an error occurs while accessing the ID3 tag
	 */
	public static Bitmap getCoverArtFromId3Tag(final File file, final int width, final int
			height) throws IOException {
		try {
			final Tag tag = AudioFileIO.read(file).getTag();
			final Artwork artwork = tag == null ? null : tag.getFirstArtwork();
			return artworkToBitmap(artwork, width, height);
		} catch (final Exception e) {
			throw new LibraryReadException("Cannot read ID3 tag from file " + file, e);
		}
	}

	/**
	 * Converts an image from an Artwork object to a Bitmap object. The supplied dimensions are used
	 * to optimise the image so that memory usage is reduced without distortion or degradation. If
	 * either dimension parameter is less than or equal to 0, then the full unoptimised cover art is
	 * returned.
	 *
	 * @param artwork
	 * 		the artwork to convert
	 * @param width
	 * 		the desired width of the image
	 * @param height
	 * 		the desired height of the image
	 * @return the converted image
	 */
	private static Bitmap artworkToBitmap(final Artwork artwork, final int width, final int
			height) {
		final byte[] rawBitmapArray = (artwork == null) ? null : artwork.getBinaryData();

		if (rawBitmapArray == null) {
			return null;
		} else if (width == 0 || height == 0) {
			return BitmapFactory.decodeByteArray(rawBitmapArray, 0, rawBitmapArray.length);
		} else {
			return BitmapEfficiencyHelper.decodeByteArray(rawBitmapArray, width, height);
		}
	}

	/**
	 * Metadata fields which can be accessed by the {@link Id3Util}.
	 */
	public enum MetadataField {
		/**
		 * Key for accessing the title of the media.
		 */
		TITLE(FieldKey.TITLE),

		/**
		 * Key for accessing the album of the media.
		 */
		ALBUM(FieldKey.ALBUM),

		/**
		 * Key for accessing the artist of the media. This may be different from the data accessed
		 * by the {@code ALBUM_ARTIST} key.
		 */
		ARTIST(FieldKey.ARTIST),

		/**
		 * Key for accessing the album artist of the media. This may be different from the data
		 * accessed by the {@code ARTIST} key.
		 */
		ALBUM_ARTIST(FieldKey.ALBUM_ARTIST),

		/**
		 * Key for accessing the arranger of the media.
		 */
		ARRANGER(FieldKey.ARRANGER),

		/**
		 * Key for accessing the beats-per-minute of the media.
		 */
		BPM(FieldKey.BPM),

		/**
		 * Key for accessing the comments of the media.
		 */
		COMMENT(FieldKey.COMMENT),

		/**
		 * Key for accessing the composer of the media.
		 */
		COMPOSER(FieldKey.COMPOSER),

		/**
		 * Key for accessing the disc number of the media.
		 */
		DISC_NUMBER(FieldKey.DISC_NO),

		/**
		 * Key for accessing the total number of discs in the collection the media belongs to.
		 */
		DISC_COUNT(FieldKey.DISC_TOTAL),

		/**
		 * Key for accessing the track number of the media.
		 */
		TRACK_NUMBER(FieldKey.TRACK),

		/**
		 * Key for accessing the total number of tracks on the disc the media belongs to.
		 */
		TRACK_COUNT(FieldKey.TRACK_TOTAL),

		/**
		 * Key for determining whether or not the media is a compilation.
		 */
		IS_COMPILATION(FieldKey.IS_COMPILATION),

		/**
		 * Key for accessing the language of the media.
		 */
		LANGUAGE(FieldKey.LANGUAGE),

		/**
		 * Key for accessing the lyricist of the media.
		 */
		LYRICIST(FieldKey.LYRICIST),

		/**
		 * Key for accessing the original artist of the media.
		 */
		ORIGINAL_ARTIST(FieldKey.ORIGINAL_ARTIST),

		/**
		 * Key for accessing the original album of the media.
		 */
		ORIGINAL_ALBUM(FieldKey.ORIGINAL_ALBUM),

		/**
		 * Key for accessing the original year of the media.
		 */
		ORIGINAL_YEAR(FieldKey.ORIGINAL_YEAR),

		/**
		 * Key for accessing the quality of the media.
		 */
		QUALITY(FieldKey.QUALITY),

		/**
		 * Key for accessing the release year of the media.
		 */
		YEAR(FieldKey.YEAR);

		/**
		 * Constructor definition for the MetadataField enum.
		 *
		 * @param fieldKey
		 * 		the FieldKey which can be passed to {@link #getMetadataFromId3Tag(File, MetadataField)}
		 * 		to obtain this metadata
		 */
		MetadataField(final FieldKey fieldKey) {
			this.fieldKey = fieldKey;
		}

		/**
		 * The FieldKey of this MetadataField.
		 */
		private final FieldKey fieldKey;
	}
}