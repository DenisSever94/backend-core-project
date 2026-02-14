@param ru.mentee.power.crm.domain.Lead lead

@template.layout.main(
content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Добавить нового лида</h1>

        <form action="/leads" method="post" class="space-y-4">
            <div>
                <label for="company" class="block text-sm font-medium text-gray-700 mb-1">
                    Company
                </label>
                <input
                        type="company"
                        id="company"
                        name="company"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="company"
                />
            </div>

            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">
                    Статус <span class="text-red-500">*</span>
                </label>
                <select id="status" name="status" required class="w-full px-3 py-2 border border-gray-300 rounded-md">
                    @if(lead != null && lead.status() == ru.mentee.power.crm.domain.LeadStatus.NEW)
                        <option value="NEW" selected>NEW</option>
                    @else
                        <option value="NEW">NEW</option>
                    @endif

                    @if(lead != null && lead.status() == ru.mentee.power.crm.domain.LeadStatus.CONTACTED)
                        <option value="CONTACTED" selected>CONTACTED</option>
                    @else
                        <option value="CONTACTED">CONTACTED</option>
                    @endif

                    @if(lead != null && lead.status() == ru.mentee.power.crm.domain.LeadStatus.QUALIFIED)
                        <option value="QUALIFIED" selected>QUALIFIED</option>
                    @else
                        <option value="QUALIFIED">QUALIFIED</option>
                    @endif
                </select>
            </div>

            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">
                    Email
                </label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="example@company.com"
                />
            </div>

            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">
                    Phone
                </label>
                <input
                        type="phone"
                        id="phone"
                        name="phone"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="phone"
                />
            </div>



            <div>
                <label for="city" class="block text-sm font-medium text-gray-700 mb-1">
                    City
                </label>
                <input
                        type="city"
                        id="city"
                        name="city"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="city"
                />
            </div>

            <div>
                <label for="street" class="block text-sm font-medium text-gray-700 mb-1">
                    Street
                </label>
                <input
                        type="street"
                        id="street"
                        name="street"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="street"
                />
            </div>

            <div>
                <label for="zip" class="block text-sm font-medium text-gray-700 mb-1">
                    Zip
                </label>
                <input
                        type="zip"
                        id="zip"
                        name="zip"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                        placeholder="zip"
                />
            </div>


            <!-- Submit Button -->
            <button
                    type="submit"
                    class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-900 focus:ring-2 focus:ring-blue-500"
            >
                Создать лида
            </button>

            <!-- Cancel Link -->
            <a
                    href="/leads"
                    class="block text-center text-sm text-gray-600 hover:text-gray-900"
            >
                Отмена
            </a>
        </form>
    </div>
`
)