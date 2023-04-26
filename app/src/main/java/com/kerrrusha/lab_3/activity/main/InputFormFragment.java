package com.kerrrusha.lab_3.activity.main;

import static com.kerrrusha.lab_3.domain.entity.Order.EMPTY_ORDER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kerrrusha.lab_3.R;
import com.kerrrusha.lab_3.domain.entity.Order;
import com.kerrrusha.lab_3.domain.validating.PizzaOrderValidator;
import com.kerrrusha.lab_3.repository.OrderRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class InputFormFragment extends Fragment {

    private FormViewModel viewModel;
    private OrderRepository orderRepository;

    EditText pizzaName;
    RadioGroup radioGroup;
    LinearLayout checkboxesLinearLayout;
    Button okButton;
    TextView saveOrderResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_form, container, false);
        assert view != null;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);

        okButton.setOnClickListener(v -> saveOrder(view));
        viewModel.getNeedsToBeCleared().observe(getViewLifecycleOwner(), needsToBeCleared -> {
            if (!needsToBeCleared) {
                return;
            }
            viewModel.setNeedsToBeCleared(false);
            clearOrder();
        });
    }

    private void initialize(View view) {
        viewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);
        orderRepository = new OrderRepository(view.getContext());

        pizzaName = view.findViewById(R.id.pizzaName);
        radioGroup = view.findViewById(R.id.radio_group);
        checkboxesLinearLayout = view.findViewById(R.id.checkbox_group);
        okButton = view.findViewById(R.id.okButton);
        saveOrderResult = view.findViewById(R.id.saveOrderResult);
    }

    private void saveOrder(View view) {
        Order order = getOrder(view);
        if (order.equals(EMPTY_ORDER)) {
            return;
        }

        viewModel.setFormResult(order.toString());

        String resultMessage = "Saved to DB successfully with id=";
        try {
            Order savedOrder = orderRepository.save(order);
            resultMessage += savedOrder.getId();
        } catch (Exception e) {
            resultMessage = e.getMessage();
        }
        saveOrderResult.setText(resultMessage);
    }

    public void clearOrder() {
        pizzaName.setText(EMPTY);
        saveOrderResult.setText(EMPTY);
        radioGroup.clearCheck();
        clearCheckFromCheckboxes();
    }

    private void clearCheckFromCheckboxes() {
        int childCount = checkboxesLinearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = checkboxesLinearLayout.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setChecked(false);
            }
        }
    }

    private Order getOrder(@NonNull View view) {
        final String SEPARATOR = "; ";

        String pizzaNameText = pizzaName.getText().toString();
        int selectedPizzaSizeRadioButtonId = radioGroup.getCheckedRadioButtonId();
        List<CheckBox> checkedCheckBoxes = getCheckedCheckboxes();

        PizzaOrderValidator validator = new PizzaOrderValidator(pizzaNameText, selectedPizzaSizeRadioButtonId);
        if (!validator.getErrors().isEmpty()) {
            showPopupWindow(String.join(SEPARATOR, validator.getErrors()));
            return EMPTY_ORDER;
        }

        Order result = new Order();

        result.setName(pizzaNameText);
        String sizeWeightCostOption = ((RadioButton) view.findViewById(selectedPizzaSizeRadioButtonId)).getText().toString();
        result.setSizeWeightCostOption(sizeWeightCostOption);

        StringBuilder additionalCostOptions = new StringBuilder();
        checkedCheckBoxes.forEach(e -> additionalCostOptions.append(e.getText().toString()).append(SEPARATOR));
        result.setAdditionCostOptions(additionalCostOptions.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result.setCreatedAt(Instant.now());
        }

        return result;
    }

    private void showPopupWindow(String message) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        popupWindow.setFocusable(true);

        TextView popupText = popupView.findViewById(R.id.popup_text);
        popupText.setText(message);

        Button closeButton = popupView.findViewById(R.id.popup_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    private List<CheckBox> getCheckedCheckboxes() {
        int childCount = checkboxesLinearLayout.getChildCount();
        List<CheckBox> checkedCheckBoxes = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            View view = checkboxesLinearLayout.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    checkedCheckBoxes.add(checkBox);
                }
            }
        }

        return checkedCheckBoxes;
    }

}
