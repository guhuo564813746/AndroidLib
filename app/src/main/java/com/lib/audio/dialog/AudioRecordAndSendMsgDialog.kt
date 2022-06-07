package com.lib.audio.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.ScreenUtils
import com.lib.code.R


/**
 *作者：create by 张金 on 2022/6/6 11:50
 *邮箱：564813746@qq.com
 */
class AudioRecordAndSendMsgDialog : DialogFragment(){
    private val TAG="AudioRecordAndSendMsgD"
    private var curX=0f
    private var curY=0f
    private var screenHeight=0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG,"onCreateDialog--")
        activity?.let {
            val layoutInflater=it.layoutInflater
            val view=layoutInflater.inflate(R.layout.audio_recording_dialog_lay,null)
            val dialog=Dialog(it,R.style.FULLDIALOG)
            val window=dialog.window
            window?.setGravity(Gravity.BOTTOM)
            window?.decorView?.setPadding(0,0,0,0)
            val windowLp=window?.attributes
            windowLp?.width=WindowManager.LayoutParams.MATCH_PARENT
            windowLp?.height=WindowManager.LayoutParams.MATCH_PARENT
            window?.attributes=windowLp
            return dialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreateView--")
        screenHeight=ScreenUtils.getScreenHeight()
        val view=inflater.inflate(R.layout.audio_recording_dialog_lay,container,false)
        val audioRecordRoot=view.findViewById<ConstraintLayout>(R.id.audio_record_root)
        audioRecordRoot.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN ->{

                    }
                    MotionEvent.ACTION_MOVE ->{
                        curX=event?.x?:0f
                        curY=event?.y?:0f
                        Log.d(TAG,"curX--${curX}--curY--${curY}--screenHeight--${screenHeight}")
                    }
                    MotionEvent.ACTION_UP ->{

                    }
                }
                return true
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated--")
    }
}