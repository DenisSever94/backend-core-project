@param java.util.List<ru.mentee.power.crm.domain.StatusLead> statusLead

@template.layout.main(
content = @`
    <div class="max-w-2xl mx-auto mt-10 px-4">
        <div class="bg-white rounded-xl shadow-md p-6">
            <div class="flex justify-between items-center mb-6 border-b pb-4">
                <h1 class="text-2xl font-bold text-gray-800">Статусы лидов</h1>
                <a href="/leads/status/new"
                   class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors text-sm font-medium">
                    + Добавить статус
                </a>
            </div>

            @if(statusLead.isEmpty())
                <div class="text-center py-10">
                    <p class="text-gray-400 text-lg">Статусов пока нет</p>
                    <p class="text-gray-300 text-sm mt-1">Нажмите кнопку выше чтобы добавить первый</p>
                </div>
            @else
                <div class="space-y-2">
                    @for(var status : statusLead)
                        <div class="flex items-center px-4 py-3 bg-gray-50 rounded-lg border border-gray-100 hover:bg-blue-50 hover:border-blue-200 transition-colors">
                            <span class="w-2 h-2 rounded-full bg-blue-400 mr-3"></span>
                            <span class="text-gray-700 font-medium">${status.name()}</span>
                        </div>
                    @endfor
                </div>
            @endif
        </div>
    </div>
`
)