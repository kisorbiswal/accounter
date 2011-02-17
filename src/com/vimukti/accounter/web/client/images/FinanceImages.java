package com.vimukti.accounter.web.client.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface FinanceImages extends ClientBundle {
	ImageResource Accounts();

	@Source("error-icon.png")
	ImageResource errorImage();

	@Source("information-icon.png")
	ImageResource information();

	@Source("warn-icon.png")
	ImageResource warning();

	@Source("blank_16x16.png")
	ImageResource balnkImage();

	ImageResource voided();

	@Source("cancel.png")
	ImageResource rejected();

	@Source("Tick-mark.png")
	ImageResource tickMark();

	@Source("before-reject.png")
	ImageResource beforereject();

	@Source("not-void.png")
	ImageResource notvoid();

	@Source("delete-success.png")
	ImageResource delSuccess();

	@Source("loader.gif")
	ImageResource loadingImage();

	@Source("dialog_help.png")
	ImageResource helpIcone();

	@Source("dialog_close.png")
	ImageResource closeBtn();

	@Source("helpicon.png")
	ImageResource help();

	@Source("close-icon.png")
	ImageResource close();

	@Source("not-void.png")
	ImageResource notdelete();

	@Source("delete.png")
	ImageResource delete();

	@Source("calendarPicker.png")
	ImageResource calendarPicker();

	@Source("information-icon-new.png")
	ImageResource companyInformation();

}
