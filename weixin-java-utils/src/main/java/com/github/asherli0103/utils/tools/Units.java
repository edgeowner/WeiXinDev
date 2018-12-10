/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.asherli0103.utils.tools;


/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public enum Units {

    days(24 * 3600 * 1e9), hours(3600 * 1e9), minutes(60 * 1e9),
    seconds(1e9), milliseconds(1e6), microseconds(1e3),
    nanoseconds(1);

    private double factor;

    Units(double f) {
        factor = f;
    }

    public static double conversionFactor(Units fromUnits, Units toUnits) {
        return (fromUnits.factor) / toUnits.factor;
    }

    public static Units fromString(String unitsString) {
        Units unit = null;
        try {
            if (unitsString == null || unitsString.length() == 0) {
                Assert.fail("Parse of units from \"" + unitsString + "\" failed!");
            }
            if (unitsString.equals(microseconds.toShortString())) {
                unit = microseconds;
            } else {
                switch (unitsString.charAt(0)) {
                    case 'd':
                        unit = days;
                        break;
                    case 'h':
                        unit = hours;
                        break;
                    case 's':
                        unit = seconds;
                        break;
                    case 'n':
                        unit = nanoseconds;
                        break;
                    case 'm':
                        if (unitsString.length() == 1) {
                            unit = minutes;
                            break;
                        } else {
                            switch (unitsString.charAt(1)) {
                                case 'i':
                                    if (unitsString.length() <= 2) {
                                        Assert.fail("Parse of units from \"" + unitsString + "\" failed!");
                                    } else {
                                        switch (unitsString.charAt(2)) {
                                            case 'n':
                                                unit = minutes;
                                                break;
                                            case 'l':
                                                unit = milliseconds;
                                                break;
                                            case 'c':
                                                unit = microseconds;
                                                break;
                                            default:
                                                Assert.fail("Parse of units from \"" + unitsString + "\" failed!");
                                        }
                                    }
                                    break;
                                case 's':
                                    unit = milliseconds;
                                    break;
                                default:
                                    Assert.fail("Parse of units from \"" + unitsString + "\" failed!");
                            }
                        }
                        break;
                    default:
                        Assert.fail("Parse of units from \"" + unitsString + "\" failed!");
                }
            }
            if (!unitsString.equals(unit.toString()) && !unitsString.equals(unit.toShortString())) {
                Assert.fail("Parse of units from \"" + unitsString + "\" failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unit;
    }

    public String toShortString() {
        switch (this) {
            case days:
                return "d";
            case hours:
                return "h";
            case minutes:
                return "m";
            case seconds:
                return "s";
            case milliseconds:
                return "ms";
            case microseconds:
                return "\u00B5s";
            case nanoseconds:
                return "ns";
            default:
                return null;
        }
    }

}
