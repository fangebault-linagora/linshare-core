/*
 *    This file is part of Linshare.
 *
 *   Linshare is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as
 *   published by the Free Software Foundation, either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   Linshare is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public
 *   License along with Foobar.  If not, see
 *                                    <http://www.gnu.org/licenses/>.
 *
 *   (c) 2008 Groupe Linagora - http://linagora.org
 *
*/
package org.linagora.linshare.core.domain.vo;

/**
 */
public enum DocToSignContext {
    DOCUMENT(0), SHARED(1);

    private int value;

    private DocToSignContext(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static DocToSignContext fromInt(int value) {
        switch(value) {
            case 0 : return DOCUMENT; //list of simple documents
            case 1 : return SHARED; //shared documents
            default : throw new IllegalArgumentException("not a valid context, check the type of document");
        }
    }
}