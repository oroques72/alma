function focusElement(element) {
  if (document.getElementById(element)) {
    document.getElementById(element).focus();
  }
}



function getRadioValue(element) {
  var radio = document.getElementsByName(element);
  for (var i = 0; i < radio.length; i++) {
    if (radio[i].checked) {
      return (radio[i].value);
    }
  }
}
