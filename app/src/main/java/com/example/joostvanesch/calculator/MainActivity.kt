package com.example.joostvanesch.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "StatePendingOperation"
private const val STATE_OPERAND_1 = "StateOperand1"
private const val STATE_OPERAND_1_STORED = "StateOperand1Stored"

class MainActivity : AppCompatActivity() {

    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()

            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }

            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonEquals.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)

        val signListener = View.OnClickListener { v ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    newNumber.setText("")
                }
            }
        }

        buttonSign.setOnClickListener(signListener)

    }

    private fun performOperation(operand2: Double, operation: String) {
        if (operand1 == null) {
            operand1 = operand2
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = operand2
                "+" -> operand1 = operand1!!.plus(operand2)
                "-" -> operand1 = operand1!!.minus(operand2)
                "*" -> operand1 = operand1!!.times(operand2)
                "/" -> operand1 = if (operand2 == 0.0) {
                                        Double.NaN
                                    } else {
                                        operand1!!.div(operand2)
                                    }
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState?.getBoolean(STATE_OPERAND_1_STORED)!!) {
                        savedInstanceState.getDouble(STATE_OPERAND_1)
                    } else {
                        null
                    }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operation.text = pendingOperation
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState?.putDouble(STATE_OPERAND_1, operand1!!)
            outState?.putBoolean(STATE_OPERAND_1_STORED, true)
        }
        outState?.putString(STATE_PENDING_OPERATION, pendingOperation)
    }
}