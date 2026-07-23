package com.flatcode.multiwave.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flatcode.multiwave.R
import com.flatcode.multiwave.databinding.FragmentWaveConsoleBinding
import com.flatcode.multiwave.ui.util.applyNavigationBarPadding
import com.google.android.material.slider.Slider
import io.selimdawa.multiwave.ShapeType
import kotlin.math.roundToInt

class WaveConsoleFragment : Fragment() {

    private var _binding: FragmentWaveConsoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaveConsoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.consoleCard.applyNavigationBarPadding()

        // Sync initial states
        binding.seekVelocity.value =
            (binding.multiWaveHeader.velocity * 10).roundToInt().toFloat().coerceIn(0f, 100f)
        binding.seekProgress.value =
            (binding.multiWaveHeader.progress * 100).roundToInt().toFloat().coerceIn(0f, 100f)
        binding.switchRunning.isChecked = binding.multiWaveHeader.isRunning
        binding.switchDirection.isChecked = binding.multiWaveHeader.scaleY == -1f

        // Listeners
        val changeListener = Slider.OnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                when (slider.id) {
                    R.id.seekProgress -> binding.multiWaveHeader.progress = value / 100
                    R.id.seekVelocity -> binding.multiWaveHeader.velocity = value / 10
                }
            }
        }

        binding.seekProgress.addOnChangeListener(changeListener)
        binding.seekVelocity.addOnChangeListener(changeListener)

        binding.seekNumber.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                val value = slider.value.toInt()
                if (value == 2) {
                    binding.multiWaveHeader.setWaves("0,0,1,1,25\n90,0,1,1,25")
                } else if (value > 0) {
                    val waves =
                        "70,25,1.4,1.4,-26\n100,5,1.4,1.2,15\n420,0,1.15,1,-10\n520,10,1.7,1.5,20\n220,0,1,1,-15".split(
                            "\n"
                        )
                    binding.multiWaveHeader.setWaves(
                        waves.subList(0, value.coerceAtMost(waves.size)).joinToString("\n")
                    )
                }
            }
        })

        binding.switchRunning.setOnCheckedChangeListener { _, value ->
            if (value) binding.multiWaveHeader.start() else binding.multiWaveHeader.stop()
        }

        binding.switchDirection.setOnCheckedChangeListener { _, value ->
            binding.multiWaveHeader.scaleY = if (value) -1f else 1f
        }

        binding.sliderStartColor.setOnColorSelectedListener { color ->
            binding.multiWaveHeader.startColor = color
        }

        binding.sliderCloseColor.setOnColorSelectedListener { color ->
            binding.multiWaveHeader.closeColor = color
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioRect -> binding.multiWaveHeader.shape = ShapeType.Rect
                R.id.radioOval -> binding.multiWaveHeader.shape = ShapeType.Oval
                R.id.radioRoundRect -> binding.multiWaveHeader.shape = ShapeType.RoundRect
            }
        }
    }
}