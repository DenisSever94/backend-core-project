@param java.util.List<ru.mentee.power.crm.domain.Lead> leads
@param String search
@param String status

<div class="mb-4 flex gap-2 items-start px-8">
    <a href="/leads"
       class="mt-3 px-4 py-1 rounded ${status == null ? "bg-blue-900 text-white" : "bg-gray-200"}">
        Все
    </a>

    <a href="/leads?status=NEW"
       class="mt-3 px-4 py-1 rounded ${"NEW".equals(status)? "bg-blue-900 text-white" : "bg-gray-200"}">
        НОВЫЙ
    </a>
    <a href="/leads?status=CONTACTED"
       class="mt-3 px-4 py-1 rounded ${"CONTACTED".equals(status) ? "bg-blue-900 text-white" : "bg-gray-200"}">
        СВЯЗАТЬСЯ
    </a>
    <a href="/leads?status=QUALIFIED"
       class="mt-3 px-4 py-1 rounded ${"QUALIFIED".equals(status)  ? "bg-blue-900 text-white" : "bg-gray-200"}">
        КВАЛИФИЦИРОВАННЫЙ
    </a>
    <a href="/leads/new" class="mt-3 ml-auto bg-green-500 hover:bg-green-600 text-white px-4 py-1 rounded flex items-center gap-1">
        <span class="font-bold">+</span> Добавить лида
    </a>
</div>

@if(status != null)
    <p class="ml-8 mt-3 text-sm text-gray-700 mb-2">
        Показаны лиды со статусом:
        <span class="ml-2 px-3 py-1 bg-green-100 text-green-800 font-medium rounded-full border border-green-200">
    ${status}
  </span>
    </p>
@endif
@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <form method="get" action="/leads"
              class="bg-white shadow rounded-lg p-4 mb-6">
            <input type="text" name="search"
                   value="${search}" placeholder="Поиск...">
            <select name="status">
                <option value="">Все статусы</option>
                <option value="NEW">NEW</option>
                <option
                        value="CONTACTED">CONTACTED</option>
                <option
                        value="QUALIFIED">QUALIFIED</option>
            </select>
            <button type="submit">Найти</button>
            <a href="/leads">Сбросить</a>
        </form>

        <h2 class="text-2xl font-bold mb-4">Lead List</h2>

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 text-left">Компания</th>
                <th class="px-4 py-2 text-left">Статус</th>
                <th class="px-4 py-2 text-left">Почта</th>
                <th class="px-4 py-2 text-left">Телефон</th>
                <th class="px-4 py-2 text-left">Город</th>
                <th class="px-4 py-2 text-left">Улица</th>
                <th class="px-4 py-2 text-left">Индекс</th>
            </tr>
            </thead>
            <tbody>
            @for(var lead : leads)
                <tr class="border-t hover:bg-gray-50">
                    <td class="px-4 py-2">${lead.company()}</td>
                    <td class="px-4 py-2">
                            <span class="px-2 py-1 rounded text-sm bg-green-100 text-green-800">
                                ${lead.status()}
                            </span>
                    <td class="px-4 py-2">${lead.contact().email()}</td>
                    </td>
                    <td class="px-4 py-2">${lead.contact().phone()}</td>
                    <td class="px-4 py-2">${lead.contact().address().city()}</td>
                    <td class="px-4 py-2">${lead.contact().address().street()}</td>
                    <td class="px-4 py-2">${lead.contact().address().zip()}</td>
                    <td class="px-4 py-2">
                        <a href="/leads/${lead.id().toString()}/edit"
                           class="text-blue-600 hover:underline">Редактировать</a>
                        <form method="post" action="/leads/${lead.id().toString()}/delete" class="inline"
                              onclick="return confirm('Удалить лида ${lead.company()}?')">
                            <button type="submit" class="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700">
                                Удалить
                            </button>
                        </form>
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`)