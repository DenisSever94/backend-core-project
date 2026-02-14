@param java.util.List<ru.mentee.power.crm.domain.Lead> leads
@param ru.mentee.power.crm.domain.LeadStatus currentFilter
<div class="mb-4 flex gap-2 items-start px-8">
    <a href="/leads"
       class="mt-3 px-4 py-1 rounded ${currentFilter == null ? "bg-blue-900 text-white" : "bg-gray-200"}">
        Все
    </a>

    <a href="/leads?status=NEW"
       class="mt-3 px-4 py-1 rounded ${currentFilter == ru.mentee.power.crm.domain.LeadStatus.NEW ? "bg-blue-900 text-white" : "bg-gray-200"}">
        NEW
    </a>
    <a href="/leads?status=CONTACTED"
       class="mt-3 px-4 py-1 rounded ${currentFilter == ru.mentee.power.crm.domain.LeadStatus.CONTACTED ? "bg-blue-900 text-white" : "bg-gray-200"}">
        CONTACTED
    </a>
    <a href="/leads?status=QUALIFIED"
       class="mt-3 px-4 py-1 rounded ${currentFilter == ru.mentee.power.crm.domain.LeadStatus.QUALIFIED  ? "bg-blue-900 text-white" : "bg-gray-200"}">
        QUALIFIED
    </a>
    <a href="/leads/new" class="mt-3 ml-auto bg-green-500 hover:bg-green-600 text-white px-4 py-1 rounded flex items-center gap-1">
        <span class="font-bold">+</span> Добавить лида
    </a>
</div>

@if(currentFilter != null)
    <p class="ml-8 mt-3 text-sm text-gray-700 mb-2">
        Показаны лиды со статусом:
        <span class="ml-2 px-3 py-1 bg-green-100 text-green-800 font-medium rounded-full border border-green-200">
    ${currentFilter}
  </span>
    </p>
@endif
@template.layout.main(content = @`
    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Lead List</h2>

        <table class="min-w-full bg-white border border-gray-200">
            <thead class="bg-gray-100">
            <tr>
                <th class="px-4 py-2 text-left">Company</th>
                <th class="px-4 py-2 text-left">Status</th>
                <th class="px-4 py-2 text-left">Email</th>
                <th class="px-4 py-2 text-left">Phone</th>
                <th class="px-4 py-2 text-left">City</th>
                <th class="px-4 py-2 text-left">Street</th>
                <th class="px-4 py-2 text-left">Zip</th>
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
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`)