function changeStatus(priceSelected) {

}



function changeSelect(select, text, input) {
  var value = select.options[select.selectedIndex].value;
  if (value == 'Autre') {
    text.style.display = 'block';
    input.focus();
  } else {
    text.style.display = 'none';
  }
}
