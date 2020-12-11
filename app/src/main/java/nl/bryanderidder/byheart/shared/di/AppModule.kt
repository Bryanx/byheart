package nl.bryanderidder.byheart.shared.di

import nl.bryanderidder.byheart.card.CardRepository
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileRepository
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.database.CardDatabase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

@JvmField
val appModule: Module = module {
    single { CardDatabase.getDatabase(get()) }
    single { get<CardDatabase>().cardDao() }
    single { get<CardDatabase>().pileDao() }
    single { CardRepository(get()) }
    single { PileRepository(get()) }

    viewModel { SessionViewModel(get()) }
    viewModel { CardViewModel(get(), get()) }
    viewModel { PileViewModel(get(), get()) }
}