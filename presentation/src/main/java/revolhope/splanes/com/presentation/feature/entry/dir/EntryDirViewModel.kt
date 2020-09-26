package revolhope.splanes.com.presentation.feature.entry.dir

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import revolhope.splanes.com.domain.usecase.entry.InsertEntryUseCase
import revolhope.splanes.com.presentation.common.base.BaseViewModel

class EntryDirViewModel @ViewModelInject constructor(
    private val insertEntryUseCase: InsertEntryUseCase,
    @Assisted private val state: SavedStateHandle
) : BaseViewModel() {



}