package com.walhalla.horolib.beans

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by combo on 29.05.2017.
 */


@Keep
class ZodiacSignAstro : Serializable {
    @JvmField
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @JvmField
    @SerializedName("name")
    @Expose
    var name: String? = null

    @JvmField
    @SerializedName("color")
    @Expose
    var color: Int = 0

    //    public int getSteps() {
    //        return steps;
    //    }
    //
    //    public void setSteps(int steps) {
    //        this.steps = steps;
    //    }
    //
    //    public int getRate() {
    //        return rate;
    //    }
    //
    //    public void setRate(int rate) {
    //        this.rate = rate;
    //    }
    //
    //    public int getLock() {
    //        return lock;
    //    }
    //
    //    public void setLock(int lock) {
    //        this.lock = lock;
    //    }
    //
    //    public int getOrder() {
    //        return order;
    //    }
    //
    //    public void setOrder(int order) {
    //        this.order = order;
    //    }
    @JvmField
    @SerializedName("date")
    @Expose
    var date: String? = null

    @JvmField
    @SerializedName("icon")
    @Expose
    var icon: Int = 0


    @SerializedName("icon")
    @Expose
    var signIcon = 0


    constructor(name: String?, date: String?, icon: Int, color: Int) {
        this.name = name
        this.date = date
        this.icon = icon
        this.color = color
    }

    constructor(id: Int, name: String?, date: String?, icon: Int, icon_2: Int, color: Int) {
        this.id = id
        this.signIcon = icon_2
        this.name = name
        this.date = date
        this.icon = icon
        this.color = color
    }

    //    public int getSteps() {
    //        return steps;
    //    }
    //
    //    public void setSteps(int steps) {
    //        this.steps = steps;
    //    }
    //
    //    public int getRate() {
    //        return rate;
    //    }
    //
    //    public void setRate(int rate) {
    //        this.rate = rate;
    //    }
    //
    //    public int getLock() {
    //        return lock;
    //    }
    //
    //    public void setLock(int lock) {
    //        this.lock = lock;
    //    }
    //
    //    public int getOrder() {
    //        return order;
    //    }
    //
    //    public void setOrder(int order) {
    //        this.order = order;
    //    }

//    fun getDate(): String? {
//        return field
//    }
//
//    fun setDate(date: String?) {
//        field = date
//    }


    //    @Override
    //    public String toString() {
    //        return ToStringBuilder.reflectionToString(this);
    //    }
    //
    //
    //    public List<Description> getDescription() {
    //        return description;
    //    }
    //
    //    public void setDescription(List<Description> description) {
    //        this.description = description;
    //    }
    constructor()

    override fun toString(): String {
        return "ZodiacSign{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", date='" + date + '\'' +
                ", icon=" + icon +
                ", signIcon=" + signIcon +
                '}'
    }
}