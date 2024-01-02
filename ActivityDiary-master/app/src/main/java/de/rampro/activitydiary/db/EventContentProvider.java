/*
 * ActivityDiary
 *
 * Copyright (C) 2023 Raphael Mack http://www.raphael-mack.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.rampro.activitydiary.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.sql.SQLException;

public class EventContentProvider extends ContentProvider {
    // 定义一个帮助类来管理数据库创建和版本管理
    private EventDatabaseHelper dbHelper;
    public static final String AUTHORITY = "de.rampro.activitydiary.eventprovider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_EVENTS = "events";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();


    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.event";
    private static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.example.event";

    private LocalDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new LocalDBHelper(getContext());
        return true;
    }

    // 实现其他必需的方法，如 query, insert, update, delete 等
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor;

        // 这里可以使用UriMatcher来处理不同的URI请求
        // 简化起见，我们假设只处理一种URI
        cursor = db.query(EventDatabaseHelper.TABLE_EVENTS, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    // 这是一个简化的 insert 方法实现
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = db.insert("events", null, values); // 使用正确的表名
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        try {
            throw new SQLException("Failed to add a record into " + uri);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = db.delete("events", selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated = db.update("events", values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        // 这里可以使用UriMatcher来处理不同的URI类型
        // 简化起见，我们假设只有一种类型
        return CONTENT_DIR_TYPE;
    }



}
