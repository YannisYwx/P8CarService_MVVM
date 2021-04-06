package com.p8.common.bean

import android.os.Parcel
import android.os.Parcelable
import com.p8.common.Constants

/**
 *  @author : WX.Y
 *  date : 2021/3/18 15:56
 *  description :
 */
data class LoginInfo(var d: String?,val name: String?,var phone: String?,var token: String?,var loginName: String?,var facadeImg: String?,var password: String?) : Parcelable {
     var userType: Int = Constants.UserType.LAND

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        userType = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(d)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(token)
        parcel.writeString(loginName)
        parcel.writeString(facadeImg)
        parcel.writeString(password)
        parcel.writeInt(userType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginInfo> {
        override fun createFromParcel(parcel: Parcel): LoginInfo {
            return LoginInfo(parcel)
        }

        override fun newArray(size: Int): Array<LoginInfo?> {
            return arrayOfNulls(size)
        }
    }
}
