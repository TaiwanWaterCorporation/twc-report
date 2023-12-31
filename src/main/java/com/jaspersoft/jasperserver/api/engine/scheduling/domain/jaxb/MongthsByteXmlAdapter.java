/*
* Copyright (C) 2005 - 2009 Jaspersoft Corporation. All rights  reserved.
* http://www.jaspersoft.com.
*
* Unless you have purchased  a commercial license agreement from Jaspersoft,
* the following license terms  apply:
*
* This program is free software: you can redistribute it and/or  modify
* it under the terms of the GNU Affero General Public License  as
* published by the Free Software Foundation, either version 3 of  the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero  General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public  License
* along with this program.&nbsp; If not, see <http://www.gnu.org/licenses/>.
*/
package com.jaspersoft.jasperserver.api.engine.scheduling.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Yaroslav.Kovalchyk
 * @version $Id: MongthsByteXmlAdapter.java 22756 2012-03-23 10:39:15Z sergey.prilukin $
 */
public class MongthsByteXmlAdapter extends XmlAdapter<MonthsSortedSetWrapper, SortedSet<Byte>>{
    @Override
    public SortedSet<Byte> unmarshal(MonthsSortedSetWrapper v) throws Exception {
        SortedSet<Byte> result = null;
        if(v != null && v.getMongths() != null && !v.getMongths().isEmpty()){
            result = new TreeSet<Byte>();
            for(String value : v.getMongths())
                result.add(Byte.valueOf(value));
        }
        return result;
    }

    @Override
    public MonthsSortedSetWrapper marshal(SortedSet<Byte> v) throws Exception {
        MonthsSortedSetWrapper result = null;
        if(v != null && !v.isEmpty()){
            SortedSet<String> strings = new TreeSet<String>();
            for(Byte value : v)
                strings.add(value.toString());
            result = new MonthsSortedSetWrapper(strings);
        }
        return result;
    }
}
