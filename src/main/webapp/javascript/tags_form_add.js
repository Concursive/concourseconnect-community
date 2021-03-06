function updateTag(tagText, tagsField) {
  var tagsControl = document.getElementById(tagsField);

  if (tagsControl.value.trim().length == 0) {
	tagsControl.value = tagText;
	return;
  }
  
  var arrTags = tagsControl.value.split(",");
  var flgFound = false;

  newTags = "";
  var len = arrTags.length;
  for (i=0; i<len; i++) {
	arrTags[i] = arrTags[i].trim();
  	if (arrTags[i] != tagText) {
  	  newTags += arrTags[i]+ ", ";
  	} else {
	  flgFound = true;
  	}
  }
  
  if (!flgFound) {
	newTags = newTags + tagText;
  } else {
	newTags = newTags.substring(0, newTags.length-2);
  }

  tagsControl.value = newTags;
}