VMoptions:

minimal javafx dependencies:
--module-path /DEIN/PFAD/ZUM/javafx-sdk-VERSION/lib --add-modules=javafx.controls,javafx.fxml

all javafx:
--module-path "/DEIN/PFAD/ZUM/javafx-sdk-VERSION/lib" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web

Bei MacOSX: Make sure the checkbox "Use the -XstartOnFirstThread argument when launching with SWT" is not checked. 