package edu.rpi.tw.calendar.android;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

abstract class CursorFilter implements Cursor {

    private final Cursor cursor;
    private boolean[] matches;
    private int index = -1;
    private int count = 0;
    private int first = -1;
    private int last = 0;

    CursorFilter(Cursor cursor) {
        this.cursor = cursor;
    }

    public abstract boolean filterRow(Cursor cursor);
    
    public final CursorFilter filter() {
        count = 0;
        this.matches = new boolean[cursor.getCount()];
        for( int i = 0; i < matches.length; i++ ) {
            if ( i == 0 ) {
                matches[i] = cursor.moveToFirst() && filterRow(cursor);
            } else {
                matches[i] = cursor.moveToNext() && filterRow(cursor);
            }
            if ( matches[i] ) {
                if ( first == -1 ) {
                    first = i;
                }
                last = i;
                count++;
            }
        }
        cursor.moveToPosition(first);
        index = -1;
        return this;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getPosition() {
        return index;
    }

    @Override
    public boolean move(int offset) {
        if ( offset == 0 ) {
            return true;
        }
        int newPos = index;
        while(offset != 0) {
            if ( offset > 0 ) {
                newPos++;
            } else {
                newPos--;
            }
            if ( newPos == count ) {
                index = newPos;
                return cursor.move( cursor.getCount() - newPos );
            } else if ( newPos == -1 ) {
                index = newPos;
                return cursor.move( newPos - cursor.getCount() );
            }
            if ( matches[newPos] ) {
                offset--;
            }
        }
        index = newPos;
        return cursor.move(offset);
    }

    @Override
    public boolean moveToPosition(int position) {
        if ( position >= count ) {
            index = count;
            cursor.moveToLast();
            cursor.moveToNext();
            return false;
        }
        if ( position < 0 ) {
            index = -1;
            cursor.moveToFirst();
            cursor.moveToPrevious();
            return false;
        }
        int newIndex = 0;
        while(position > 0) {
            if ( matches[++newIndex] ) {
                position--;
            }
            if ( newIndex == matches.length ) {
                break;
            }
        }
        index = newIndex;
        return cursor.moveToPosition( newIndex );
    }

    @Override
    public boolean moveToFirst() {
        if ( cursor.moveToPosition( first ) ) {
            index = first;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveToLast() {
        if ( cursor.moveToPosition( last ) ) {
            index = last;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveToNext() {
        if ( count == 0 ) {
            return false;
        }
        index++;
        if ( index >= count ) {
            return false;
        }
        for(; index < count; index++) {
            if ( matches[index] ) {
                break;
            }
        }
        if ( cursor.moveToPosition( index ) ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveToPrevious() {
        // TODO fixme
        if ( cursor.moveToPosition(index - 1) ) {
            index--;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFirst() {
        return index == 0;
    }

    @Override
    public boolean isLast() {
        return index == count - 1;
    }

    @Override
    public boolean isBeforeFirst() {
        return index == -1;
    }

    @Override
    public boolean isAfterLast() {
        return index >= count;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return cursor.getColumnIndex(columnName);
    }

    @Override
    public int getColumnIndexOrThrow(String columnName)
            throws IllegalArgumentException {
        return cursor.getColumnIndexOrThrow(columnName);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return cursor.getColumnName(columnIndex);
    }

    @Override
    public String[] getColumnNames() {
        return cursor.getColumnNames();
    }

    @Override
    public int getColumnCount() {
        return cursor.getColumnCount();
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        return cursor.getBlob(columnIndex);
    }

    @Override
    public String getString(int columnIndex) {
        return cursor.getString(columnIndex);
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        cursor.copyStringToBuffer(columnIndex, buffer);
    }

    @Override
    public short getShort(int columnIndex) {
        return cursor.getShort(columnIndex);
    }

    @Override
    public int getInt(int columnIndex) {
        return cursor.getInt(columnIndex);
    }

    @Override
    public long getLong(int columnIndex) {
        return cursor.getLong(columnIndex);
    }

    @Override
    public float getFloat(int columnIndex) {
        return cursor.getFloat(columnIndex);
    }

    @Override
    public double getDouble(int columnIndex) {
        return cursor.getDouble(columnIndex);
    }

    @Override
    public int getType(int columnIndex) {
        return cursor.getType(columnIndex);
    }

    @Override
    public boolean isNull(int columnIndex) {
        return cursor.isNull(columnIndex);
    }

    @Override
    public void deactivate() {
        cursor.deactivate();
    }

    @Override
    public boolean requery() {
        if ( cursor.requery() ) {
            filter();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() {
        cursor.close();
    }

    @Override
    public boolean isClosed() {
        return cursor.isClosed();
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {
        cursor.registerContentObserver(observer);
    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {
        cursor.unregisterContentObserver(observer);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        cursor.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        cursor.unregisterDataSetObserver(observer);
    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {
        cursor.setNotificationUri( cr, uri );
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return cursor.getWantsAllOnMoveCalls();
    }

    @Override
    public Bundle getExtras() {
        return cursor.getExtras();
    }

    @Override
    public Bundle respond(Bundle extras) {
        return cursor.respond( extras );
    }

}
