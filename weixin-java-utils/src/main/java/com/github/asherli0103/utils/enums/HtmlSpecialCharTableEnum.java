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

package com.github.asherli0103.utils.enums;

/**
 * HTML特殊字符转意枚举类
 *
 * @author Asherli
 * @version 1.0.00
 */
@SuppressWarnings(value = {"unused"})
public enum HtmlSpecialCharTableEnum {
    ALPHA("Α", "&Alpha;"),
    DELTA("Δ", "&Delta;"),
    ETA("Η", "&Eta;"),
    KAPPA("Κ", "&Kappa;"),
    NU("Ν", "&Nu;"),
    PI("Π", "&Pi;"),
    TAU("Τ", "&Tau;"),
    CHI("Χ", "&Chi;"),
    ALPHA2("α", "&alpha;"),
    DELTA2("δ", "&delta;"),
    ETA2("η", "&eta;"),
    KAPPA2("κ", "&kappa;"),
    NU2("ν", "&nu;"),
    PI2("π", "&pi;"),
    SIGMA("σ", "&sigma;"),
    PHI("φ", "&phi;"),
    OMEGA("ω", "&omega;"),
    PIV("ϖ", "&piv;"),
    PRIME("′", "&prime;"),
    FRASL("⁄", "&frasl;"),
    REAL("ℜ", "&real;"),
    LARR("←", "&larr;"),
    DARR("↓", "&darr;"),
    LARR2("⇐", "&lArr;"),
    DARR2("⇓", "&dArr;"),
    PART("∂", "&part;"),
    NABLA("∇", "&nabla;"),
    NI("∋", "&ni;"),
    MINUS("−", "&minus;"),
    PROP("∝", "&prop;"),
    AND("∧", "&and;"),
    CUP("∪", "&cup;"),
    SIM("∼", "&sim;"),
    NE("≠", "&ne;"),
    GE("≥", "&ge;"),
    NSUB("⊄", "&nsub;"),
    OPLUS("⊕", "&oplus;"),
    SDOT("⋅", "&sdot;"),
    LFLOOR("「", "&lfloor;"),
    SPADES("♠", "&spades;"),
    DIAMS("♦", "&diams;"),
    CENT("¢", "&cent;"),
    YEN("¥", "&yen;"),
    UML("¨", "&uml;"),
    LAQUO("«", "&laquo;"),
    REG("®", "&reg;"),
    PLUSMN("±", "&plusmn;"),
    ACUTE("´", "&acute;"),
    LT("<", "&lt;"),
    BATE("Β", "&Beta;"),
    EPSILON("Ε", "&Epsilon;"),
    THETA("Θ", "&Theta;"),
    LAMBDA("Λ", "&Lambda;"),
    XI("Ξ", "&Xi;"),
    RHO("Ρ", "&Rho;"),
    UPSILON("Υ", "&Upsilon;"),
    PSI("Ψ", "&Psi;"),
    BETA("β", "&beta;"),
    EPSILON2("ε", "&epsilon;"),
    THETA2("θ", "&theta;"),
    LAMBDA2("λ", "&lambda;"),
    XI2("ξ", "&xi;"),
    RHO2("ρ", "&rho;"),
    TAU2("τ", "&tau;"),
    CHI2("χ", "&chi;"),
    THETASYM("ϑ", "&thetasym;"),
    GAMMA("Γ", "&Gamma;"),
    ZETA("Ζ", "&Zeta;"),
    IOTA("Ι", "&Iota;"),
    MU("Μ", "&Mu;"),
    OMICRON("Ο", "&Omicron;"),
    SIGMA2("Σ", "&Sigma;"),
    PHI2("Φ", "&Phi;"),
    OMEGA2("Ω", "&Omega;"),
    GAMMA2("γ", "&gamma;"),
    ZETA2("ζ", "&zeta;"),
    IOTA2("ι", "&iota;"),
    MU2("μ", "&mu;"),
    OMICRON2("ο", "&omicron;"),
    SIGMAF("ς", "&sigmaf;"),
    UPSILON2("υ", "&upsilon;"),
    PSI2("ψ", "&psi;"),
    UPSIH("ϒ", "&upsih;"),
    BULL("•", "&bull;"),
    HELLOP("…", "&hellip;"),
    PTINE("″", "&Prime;"),
    OLINE("‾", "&oline;"),
    WEIERP("℘", "&weierp;"),
    IMAGE("ℑ", "&image;"),
    TRADE("™", "&trade;"),
    ALEFSYM("ℵ", "&alefsym;"),
    UARR("↑", "&uarr;"),
    RARR("→", "&rarr;"),
    HARR("↔", "&harr;"),
    CRARR2("↵", "&crarr;"),
    UARR2("⇑", "&uArr;"),
    RARR2("⇒", "&rArr;"),
    HARR2("⇔", "&hArr;"),
    FORALL("∀", "&forall;"),
    EXIST("∃", "&exist;"),
    EMPTY("∅", "&empty;"),
    ISIN("∈", "&isin;"),
    NOTIN("∉", "&notin;"),
    PROD("∏", "&prod;"),
    SUM("∑", "&sum;"),
    LOWAST("∗", "&lowast;"),
    RADIC("√", "&radic;"),
    INFIN("∞", "&infin;"),
    ANG("∠", "&ang;"),
    OR("∨", "&or;"),
    CAP("∩", "&cap;"),
    INT("∫", "&int;"),
    THERE4("∴", "&there4;"),
    CONG("≅", "&cong;"),
    ASYMP("≈", "&asymp;"),
    EQUIV("≡", "&equiv;"),
    LE("≤", "&le;"),
    SUB("⊂", "&sub;"),
    SUP("⊃", "&sup;"),
    SUBE("⊆", "&sube;"),
    SUPE("⊇", "&supe;"),
    OTIMES("⊗", "&otimes;"),
    PERP("⊥", "&perp;"),
    LCEIL("『", "&lceil;"),
    RCEIL("』", "&rceil;"),
    RFLOOR("」", "&rfloor;"),
    LOZ("◊", "&loz;"),
    CLUBS("♣", "&clubs;"),
    HEARTS("♥", "&hearts;"),
    NBSP(" ", "&nbsp;"),
    IEXCL("¡", "&iexcl;"),
    POUND("£", "&pound;"),
    CURREN("¤", "&curren;"),
    BRVBAR("¦", "&brvbar;"),
    SECT("§", "&sect;"),
    COPY("©", "&copy;"),
    ORDF("ª", "&ordf;"),
    NOT("¬", "&not;"),
    MACR("¯", "&macr;"),
    DEG("°", "&deg;"),
    SUP2("²", "&sup2;"),
    SUP3("³", "&sup3;"),
    MICRO("µ", "&micro;"),
    QUOT("\"", "&quot;"),
    GT(">", "&gt;"),
    //AMP("&","&amp;"),
    SQUOTES("'", "&#039;");

    /**
     * 原始字符
     */
    private CharSequence charValue;

    /**
     * 转义字符
     */
    private CharSequence strValue;

    /**
     * 构造方法
     *
     * @param charValue 原始字符
     * @param strValue  转义字符
     */
    HtmlSpecialCharTableEnum(CharSequence charValue, CharSequence strValue) {
        this.charValue = charValue;
        this.strValue = strValue;
    }

    /**
     * 获取原始字符
     *
     * @return 原始字符
     */
    public CharSequence getCharValue() {
        return charValue;
    }

    /**
     * 获取转义字符
     *
     * @return 转义字符
     */
    public CharSequence getStrValue() {
        return strValue;
    }

}
