package com.artkostm.flymer

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget._
import com.artkostm.flymer.communication.login.Login.AttemptLogin
import com.artkostm.flymer.view.Tweaks
import macroid.Contexts
import macroid.FullDsl._
import macroid.contrib.LpTweaks._
import com.artkostm.flymer.utils.FlymerHelper._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by artsiom.chuiko on 03/10/2016.
 */
class LoginActivity extends Activity with Contexts[Activity] {
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    var label = slot[Button]
    var email = slot[TextView]
    var password = slot[TextView]

    val mainView = l[LinearLayout] (
      w[EditText] <~ matchWidth <~ wire(email),
      w[EditText] <~ matchWidth <~ wire(password) <~ Tweaks.password,
      w[Button] <~ matchWidth <~ wire(label) <~ text("Log in")
    ) <~ vertical

    setContentView(mainView.get)

    label.get.setOnClickListener { source: View =>
      Future {
        AttemptLogin(email.get.getText, password.get.getText)
      } map { value => value match {
          case Success(loginInfo) => runOnUiThread {
            Toast.makeText(LoginActivity.this, loginInfo.toString, Toast.LENGTH_LONG).show()
//            getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE).edit().
          }
          case Failure(e) => runOnUiThread { Toast.makeText(LoginActivity.this, e.getMessage, Toast.LENGTH_LONG).show() }
        }
      }
    }
  }
}
