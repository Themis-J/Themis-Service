UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">现金折扣所得</legend>
    <div>
        <b>所含金额</b>
        此科目所含金额反映由于现金支付给供应商而获得的一些额外的折扣收入。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    目前来看，基本没有企业有此类收入。
	</div>
</div>'
WHERE id=43;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">现金折扣获准</legend>
    <div>
        <b>所含金额</b>
        此科目所含金额反映由于顾客使用现金支付而给予的一些额外的折扣费用。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    目前来看，基本没有企业有此类费用。
	</div>
</div>'
WHERE id=48;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">现金折扣获准</legend>
    <div>
        <b>所含金额</b>
        此科目所含金额反映由于顾客使用现金支付而给予的一些额外的折扣费用。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    目前来看，基本没有企业有此类费用。
	</div>
</div>'
WHERE id=44;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">利息所得</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映由于公司存款所得的利息收入。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    <span style="color:red">此账户余额反映在存款账户、应收账款或经销商提供贷款产生的利息所得 。获得利息时，所得利息金额贷计入此账户。</span>
    <br><br>
    除了实际财务上真实发生的利息收入，还包括“假想存入”的自有资金所可能获得的利息收入。这部分自有资金实际是用于购买车辆库存。体现这笔自有资金的“假想”利息收入在这里后，会同时也体现在销售部门的车辆融资利息费用项里，作为冲销。
	</div>
</div>'
WHERE id=44;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">利息所得</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映由于公司存款所得的利息收入。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    <span style="color:red">此账户余额反映在存款账户、应收账款或经销商提供贷款产生的利息所得 。获得利息时，所得利息金额贷计入此账户。</span>
    <br><br>
    除了实际财务上真实发生的利息收入，还包括“假想存入”的自有资金所可能获得的利息收入。这部分自有资金实际是用于购买车辆库存。体现这笔自有资金的“假想”利息收入在这里后，会同时也体现在销售部门的车辆融资利息费用项里，作为冲销。
	</div>
</div>'
WHERE id=44;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">坏帐费用</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映坏账的费用。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    实际财务情况为准。
	</div>
</div>'
WHERE id=58;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">坏账回收</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映坏账回收的收入。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    实际财务的情况为准。
	</div>
</div>'
WHERE id=45;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">坏账回收</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映坏账回收的收入。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    实际财务的情况为准。
	</div>
</div>'
WHERE id=45;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">董事费用</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映支付给董事的费用。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    实际财务的情况。
	</div>
</div>'
WHERE id=50;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">营业外收入</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映小额的一些营业外的收入，比如处理一些小型办公用具之类的收入。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    这里不能填入车间废旧件、废旧机油等的处理收入。那一项不属于营业外收入。
    <br><br>

    <span style="color: red">偶然情况下，经销商可能获得与汽车业务无关的收入。如，经销商有闲置的房产可以出租给租户从事其他业务。收取的租金即属于营业外收入，应计入此账户中。</span>
	</div>
</div>'
WHERE id=46;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">营业外支出</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映一些营业外的支出。<span style="color: red">反映经销商发生的与主营业务无关的其他费用支出。</span>
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    这里不包括捐赠款。
	</div>
</div>'
WHERE id=51;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">代收</legend>
    <div>
        <b>所含金额</b><br>
        此科目和代付科目是用于填写一些对企业来说只是财务中转的款项。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    凡是非经营性的、对企业来说只是一进一出的款项都可填入这里。<br>
    另外，对于客户使用贷款按揭的情况，厂家有贴息政策，有的企业给客户的贷款利息补贴超过了厂家最后返回的贴息，对于厂家贴息可以覆盖的那部分金额，放入这里的代收代付。超出的部分，计入车辆成本。
	</div>
</div>'
WHERE id=47;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">代付</legend>
    <div>
        <b>所含金额</b><br>
        此科目和代收科目是用于填写一些对企业来说只是财务中转的款项。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    凡是非经营性的、对企业来说只是一进一出的款项都可填入这里。 <br>
    另外，对于客户使用贷款按揭的情况，厂家有贴息政策，有的企业给客户的贷款利息补贴超过了厂家最后返回的贴息，对于厂家贴息可以覆盖的那部分金额，放入这里的代收代付。超出的部分，计入车辆成本。
	</div>
</div>'
WHERE id=52;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">建店返利</legend>
    <div>
        <b>所含金额</b><br>
        此科目体现新店从厂家那里拿到的建店返利。
    </div>
    <br>
</div>'
WHERE id=53;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">市场营销补贴</legend>
    <div>
        <b>所含金额</b><br>
        此科目体现从厂家那里拿到的对广宣和市场营销费用的补贴。
    </div>
    <br>
</div>'
WHERE id=54;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">满意度奖励</legend>
    <div>
        <b>所含金额</b><br>
        此科目体现从厂家那里拿到的由于顾客满意度高而获得的相关奖励。
    </div>
    <br>
</div>'
WHERE id=55;

UPDATE GeneralJournalItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">其他奖励</legend>
    <div>
        <b>所含金额</b><br>
        此科目体现从厂家那里拿到的其他未归类的奖励。
    </div>
</div>'
WHERE id=56;

UPDATE AccountReceivableDurationItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">客户 - 维修、钣喷与备件</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映客户在售后部门所产生的应收账款。
    </div>
</div>'
WHERE id=1;

UPDATE AccountReceivableDurationItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">原厂</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映从主机厂的应收账款。
    </div>
</div>'
WHERE id=3;

UPDATE AccountReceivableDurationItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">保修索赔</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映保修索赔相关的应从主机厂拿到的应收账款。
    </div>
</div>'
WHERE id=5;

UPDATE AccountReceivableDurationItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">保险理赔应收款</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映保险理赔相关的应从保险公司拿到的应收账款。
    </div>
</div>'
WHERE id=6;

UPDATE AccountReceivableDurationItem
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">分期与保险手续费</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映金融机构和保险公司尚欠经销商做按揭贷款和保险的佣金及手续费的款项金额。
    </div>
</div>'
WHERE id=7;

UPDATE AccountReceivableDurationItem
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">其它应收帐款</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映没能归入常规项目的其他应收账款。
    </div>
    <br>
    <b>说明</b>
	<div style="font-size: 12px;color: gray">
    对于像保证金之类的款项，不属于账款，不应计入应收账款里。
	</div>
</div>'
WHERE id=8;


UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">本品牌新车库存台次</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映本品牌的新车库存各个库龄类别的台次以及金额总数。
    </div>
</div>'
WHERE id=1;


UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">非本品牌新车库存台次</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映非本品牌的新车库存各个库龄类别的台次以及金额总数。
    </div>
</div>'
WHERE id=2;


UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">非本品牌新车库存台次</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映非本品牌的新车库存各个库龄类别的台次以及金额总数。
    </div>
</div>'
WHERE id=2;

UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">其它车辆库存</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映其他类别的车辆的各个库龄类别的金额总数。
    </div>
</div>'
WHERE id=3;

UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">二手车库存台次</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映二手车库存各个库龄类别的台次以及金额总数。
    </div>
</div>'
WHERE id=12;

UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">本品牌备件库存</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映本品牌备件库存各个库龄类别的金额总数。
    </div>
</div>'
WHERE id=5;

UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">本品牌精品库存</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映本品牌精品库存各个库龄类别的金额总数。
    </div>
</div>'
WHERE id=6;

UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">非本品牌备件库存</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映非本品牌备件库存各个库龄类别的金额总数。
    </div>
</div>'
WHERE id=7;

UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">非本品牌精品库存</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映非本品牌精品库存各个库龄类别的金额总数。
    </div>
    <br>
    <b>说明</b><br>
</div>'
WHERE id=8;

UPDATE InventoryDurationItem 
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">轮胎库存</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映轮胎库存各个库龄类别的金额总数。
    </div>
    <br>
    <b>说明</b><br>
</div>'
WHERE id=9;

UPDATE EmployeeFeeSummaryItem  
SET tooltip='<div style="width: 500px"  >
    <legend style="font-size: 16px">本月未结算工时</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映在每月最后一天，财务结算时，所有未完工的工单和所有完工但未与客户结算的工单中的工时成本（即技师工资）的总额。
    </div>
</div>'
WHERE id=1;

UPDATE EmployeeFeeSummaryItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">本月实际工作天数</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映当月售后车间实际工作的天数。
    </div>
    <br>
</div>'
WHERE id=2;


UPDATE EmployeeFeeSummaryItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">本月实际维修工位数量</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映当月车间里实际使用的维修工位数量。
    </div>
    <br>
</div>'
WHERE id=3;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">销售顾问以外的各部门员工薪酬</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映支付的非总经理，非部门经理、非销售顾问及售后技师的薪酬总计。以当月财务实际支付的金额填报。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>1. 支付的非总经理，非部门经理、非销售顾问及售后技师的薪酬总计</td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>

    <div style="font-size: 12px;color: gray">
        费用分配：员工工资应该记入相应部门的费用。服务于多个部门的员工工资应该公平的分摊给相应部门。
        分配比例可以按照该员工对每个部门投入的精力和时间来计算
    </div>
</div>'
WHERE id=5;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">广告费用</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映为提高经销商及其产品销售和服务而支出的费用总额。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>与新车或二手车直接相关的广告费用支出<br>有助于提高知名度或使经销商受益的广告费用<br>直接邮件促销费用，包括大宗邮件的邮资费用<br>其他广告费用<br></td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>

    <div style="font-size: 12px;color: gray">
        当只有一个部门受益于该广告时，全部费用应由这个部门承担；如果使多个部门受益时，该费用应按一定比例分配给各个部门。主要是销售和售后部门进行合理比例的分摊。<br>
        厂家支付的市场营销补贴不在这里冲销，而是集中在返利部分体现。<br>
        另外，由于为了提高CSI指数，对于厂家打电话给客户做调查，有的企业会给客户一定的礼品等让客户给高分。这笔费用也可以放入这里。
    </div>
</div>'
WHERE id=6;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">车辆短期融资信贷利息</legend>
    <div>
        <b>所含金额</b><br>
        该账户金额反映由于车辆库存占用的资金所需融资产生的利息费用。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>车辆库存（新车，二手车，展车，试乘试驾车）占用的资金所需融资产生的利息费用</td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>

    <div style="font-size: 12px;color: gray">
        该项目是用于体现销售部门为维持车辆库存而承担的利息压力。销售部门对于车辆库存的管理能力的比较就是由此科目的KPI指标的对比得出。所以，这里需要假定公司每一辆车辆，都是存货融资贷款购买的。
        <br>
        对于销售部门使用公司自有资金而不是通过融资购买库存的情况，财务部门需要计算出所使用的自有资金如果作为存款，可能获得的利息收入。将这笔“假定”的利息体现在非经营性损益的利息收入科目里，再加入本科目作为费用对冲。<br>
    </div>
</div>'
WHERE id=7;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">库存维护、修复费用</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映库存车辆在停车厂里，由于各种原因，比如移库损坏，电池更换、轮胎修补、清洁和抛光、洗车发生的费用。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>维护库存车辆的费用<br>
                    <ul>
                        <li>清洗和细节费用</li>
                        <li>汽油（库存车需要的少量的油费）</li>
                        <li>充电电池成本</li>
                        <li>车辆保养成本</li>
                        <li>事故修复成本</li>
                        <li>二手车辆预热和安全检查</li>
                        <li>二手车辆预热和安全检查</li>
                        <li>成本（不包括翻新整备修理费用）</li>
                    </ul>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>注释</b>

    <div style="font-size: 12px;color: gray">
        本科目是为了体现对于库存的管理情况。所以，对于另外租停车场的情况，停车场租金不要体现在这里，统一放入“土地租金”。
        <br>
        记录应当保存有关每个车辆的保养费用清单. 对常规服务，修理订单应完成电池重新充电，预热和安全检查。维修二手车费用应计入二手车部门，新车维修费用应计入新车部门。
    </div>
</div>'
WHERE id=8;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">公司用车费用</legend>
    <div>
        <b>所含金额</b><br>
        该账户金额反映公司内部车辆和试乘试驾车所发生的费用。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1. The expenses related to operating and maintaining Company Vehicles and short term demonstrators
                    为维护公司车辆以及试乘试驾的费用
                    <ul>
                        <li>Gasoline 汽油
                        </li>
                        <li>Car Washes 洗车
                        </li>
                        <li>Required Maintenance 保养费用
                        </li>
                        <li>Repairs 修理费
                        </li>
                        <li>License, Registration and Title Fees 牌照、年检费等
                        </li>
                    </ul>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>Comments 注释</b>

    <div style="font-size: 12px;color: gray">
        含部门经理以上配车、公务用车、试乘试驾车、代步车、救援车等的保养、洗车、汽油、车辆保险及车船使用税等。不含资产折旧。
        <br>
        总经理及领导层用车分摊入各个部门。各部门经理用车费用进入各自部门。
        <br>
        当一个部门从公司车辆获益时，全部费用应记入该部门。如，拖车费用应记入维修部。如不止一个部门获益，费用应按比例在这些部门间进行分分配。
    </div>
</div>'
WHERE id=9;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">员工差旅培训费用</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映部门经理层以下的员工培训和差旅的费用。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1. The cost of attending training meetings for dealership employees <br>
                    经销商员工参加会议培训费<br>
                    2. The cost of travel and lodging while attending training meetings <br>
                    参加培训期间的旅费和住宿费 <br>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>Comments 注释</b>

    <div style="font-size: 12px;color: gray">
        只体现部门经理以下级别的员工的差旅和培训费用。
    </div>
</div>'
WHERE id=10;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">商品运输费用</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映经销商额外调车和额外购买零件所需支付的运输费用。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    额外调车和额外购买零件所需支付的运输费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>Comments 注释</b>

    <div style="font-size: 12px;color: gray">
        该科目是用于比较由于额外需要调车或者购买零件而产生的费用。同时，也是用于体现销售部门在引导顾客购买库存车辆，而不是频繁额外调车的能力。
    </div>
</div>'
WHERE id=11;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">公司服装费用</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映为员工提供制服费以及定期洗衣费用。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    制服成本和洗衣费
                </td>
                <td>由员工自己承担的制服和洗衣费用（工资扣除）</td>
            </tr>
        </table>
    </div>
    <br>
    <b>Comments 注释</b>

    <div style="font-size: 12px;color: gray">
        就按实际财务支出的情况填写。
    </div>
</div>'
WHERE id=12;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">顾客满意度费用-维修、售后等</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映经销商为平复客户对售后部门的投诉而不得不产生的免费服务和赠品的费用成本。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    售后部门提供的免费服务和赠品的费用成本
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>Comments 注释</b>

    <div style="font-size: 12px;color: gray">
        此项费用仅仅是体现由于售后部门的错误而不得不产生的免费服务和赠品的成本。
    </div>
</div>'
WHERE id=13;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">车间低耗品及工具</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映售后车间的杂项工具和消耗品物料的费用。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	The cost of small tools and supplies such as, but not limited to:<br>
                    小工具和物资费用，包括但不限于：<br>
                    <ul>
                        <li>Screwdrivers, Utility Knives, etc.<br>螺丝起子，美工刀等</li>
                        <li>Cleaning Supplies<br>清洁用具</li>
                        <li>Light Bulbs<br>灯泡</li>
                        <li>Extension Cords<br>延长线</li>
                     </ul>
                    2.	The cost of any other supply item which cannot be categorized in other accounts<br>
                        不列入其他账户的物料
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>Comments 注释</b>

    <div style="font-size: 12px;color: gray">
        一般小工具和用品，通常寿命一年以下的物品。<br>
        当一个部门获益时，全部费用归入该部门。如不止一个部门获益时，应将费用按比例分摊到各个部门。
    </div>
</div>'
WHERE id=14;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">土地租金或按揭月供利息</legend>
    <div>
        <b>所含金额</b><br>
        该科目的金额代表了土地租金及抵押贷款利息费用等。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    土地租金及抵押贷款利息费用等
                </td>
                <td>转租经销店面设施而得的收入</td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>

    <div style="font-size: 12px;color: gray">
        此费用需要按合理比例分摊到各个部门。<br>
        如果有额外租停车场的，租金也一起计入此科目。  <br>
    </div>
</div>'
WHERE id=31;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">公司用车以外的固定资产保险</legend>
    <div>
        <b>所含金额</b><br>
        房产、设备（不含公司用车）等保险。 <br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	按月缴纳的经销商设施保险费及建筑维修费用。
                    <ul>
                        <li>火险</li>
                        <li>盗窃险</li>
                        <li>综合保险</li>
                        <li>自然灾害险</li>
                        <li>其他保险</li>
                    </ul>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=32;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">水电费用</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额代表着经销商用于经营所消耗的水、电、照明及能源等费用的合计。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    水、电、照明及能源等费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=33;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">房屋建筑物维护</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额代表了所有用于厂房及设备维护的费用总和。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	含建筑物维护性质的超出一年以上的长期待摊费用，按摊销期反映）
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        该费用应归入相关部门，如果多个部门获益，应按一定比例分摊。
    </div>
</div>'
WHERE id=34;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">房产税（按受益期归集）</legend>
    <div>
        <b>所含金额</b><br>
        该项金额表示了经销商为房产所支付的房产税。按财务实际发生为准。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	经销商为房产所支付的房产税
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        建议按各部门的使用面积比例分配该费用。
    </div>
</div>'
WHERE id=35;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">房屋折旧及购入土地摊销</legend>
    <div>
        <b>所含金额</b><br>
        该科目余额表示经销商分期支付的土地及房屋建筑物摊销费用合计。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1. 土地及房屋建筑物摊销费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        建议按各部门的使用面积比例分配该费用。
    </div>
</div>'
WHERE id=36;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">房屋外的固定资产折旧</legend>
    <div>
        <b>所含金额</b><br>
        此科目体现了除房屋以外的，包含公司用车的，固定资产折旧。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	除房屋以外的，包含公司用车的，固定资产折旧。
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        需要将此费用按合理比例分摊到相应部门。
    </div>
</div>'
WHERE id=36;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">营业税金附加及其它税费</legend>
    <div>
        <b>所含金额</b><br>
        此科目体现了营业税金附加及其他税费，应包含印花税、堤围防护费等。不含增值税、房产税、所得税、车船使用税）。<br>
    </div>
    <br>
</div>'
WHERE id=38;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">一般保险</legend>
    <div>
        <b>所含金额</b><br>
        该科目余额体现多项保险费用的合计，含库存车辆保险、火灾、意外等，不含房产、设备等保险。特别不含公司用车的保险。也不含员工福利相关的保险。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	经销商的各种保险费用。
                    <ul>
                        <li>火险及盗窃险</li>
                        <li>车库责任险</li>
                        <li>库存商品意外险等</li>
                    </ul>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        含库存车辆保险、火灾、意外等，不含房产、设备等保险。此处应不含公司用车辆的保险，公司用车的保险费按不同车辆归集，反映到“公司用车费用”。
    </div>
</div>'
WHERE id=39;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">设备维护</legend>
    <div>
        <b>所含金额</b><br>
        该科目表示所有用于维护经销商设备正常运行所需的费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	经销商设备的维修养护费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        所有动产的维护、保养，不含公司用车的维护、保养。
    </div>
</div>'
WHERE id=40;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">证照费</legend>
    <div>
        <b>所含金额</b><br>
        此科目体现所有法律规定的相关商务许可证或各类证照办理所产生的费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=41;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">其它小额固定费用</legend>
    <div>
        <b>所含金额</b><br>
        其它小额固定费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=42;



UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">员工/团队奖金</legend>
    <div>
        <b>所含金额</b><br>
        本科目体现各类一般一次的、或特殊情况、非经常性的奖励，含公司全体人员年终奖合计、安全生产奖、计划生育奖等奖金费用<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>   1.	各类一般一次的、或特殊情况、非经常性的奖励，含公司全体人员年终奖合计、安全生产奖、计划生育奖等奖金费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
</div>'
WHERE id=57;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">销售顾问薪酬</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映支付的销售顾问的薪资（底薪和提成）。以当月财务实际支付的金额填报。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>1.	支付给销售员销售新车和二手车的薪酬
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        费用分配：每项费用应分别计入新车或二手车。如，与新车销售有关的费用应计入新车部门（01） <br>
        相应的，与二手车相关的费用应计入二手车部门（02）。与两个部门均相关的费用应按各部门的获益比例进行分配。    <br>
    </div>
</div>'
WHERE id=1;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">金融保险顾问薪酬</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映支付给水平事业经理的薪资（底薪和提成）。以当月财务实际支付的金额填报。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>1.	1.	销售水平事业的销售经理获得的薪酬金额
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
</div>'
WHERE id=2;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">车辆交付费用</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映为车辆交付时所产生的相关费用。此项费用只是统计在车辆交付的过程中的一些常规发生的费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>1.	为车辆交付发生的准备和维护费
                    <ul>
                        <li>新车PDS所支付给</li>
                        <li>交车时加油费用</li>
                        <li style="color: red">售前清洗和美容费用</li>
                        <li style="color: red">二手车排量和安全检测<br>(不包括核算为翻新成本的维修) </li>
                        <li>液体高度和轮胎压力的维护 </li>
                        <li>电池充电费等 </li>
                    </ul>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
</div>'
WHERE id=3;



UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">顾客满意度费用</legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映经销商为平息顾客投诉而提供给客户的各种免费服务或产品的成本总额。 <br>
        这里体现的一定不是常规的顾客满意度项目，如每台车都有的礼品包，那个费用应进入车辆成本内。  <br>
        这里体现的主要是由于经销商的偶然性错误，而导致必须要平息客户投诉所产生的费用。同时，也包括由销售经理决定的，销售团队送出的，可送可不送，可多送也可少送的礼品之类的费用。   <br>
        各经销商应制定相应的工作制度和流程来控制和统计这一类的额外支出。
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>1.	为客户满意而提供给客户的服务或商品的成本
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=4;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">会员费、非品牌协会、出版物订阅费</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映为加入或赞助经销商组织、订购杂志期刊等发生或支付的费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	协会会员费或支付给民间和专业组织会费.
                    <ul>
                        <li>销售行业协会会费</li>
                        <li>商会费</li>
                        <li>其他商业团体费用</li>
                     </ul>
                    2.	专业期刊、杂志和其他期刊的订阅费用
                   <span style="color: red"> 3.	所需的技术出版物的成本（店手册、配件目录） </span>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        当该会费及刊物费使一个部门获益时，全部费用归该部门。如不止一个部门从中获益，该费用应按比例分摊到各部门。
    </div>
</div>'
WHERE id=19;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">办公用品</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映办公用品支出总额。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	所有办公用品，文具、纸张及打印耗材等费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        该办公用品使一个部门获益时，全部费用归该部门。如不止一个部门获益，应按比例分摊到各个部门。
    </div>
</div>'
WHERE id=20;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">外包服务费</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映外部公司为经销商提供服务发生的费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	由外部公司提供服务的费用，其他账户定义的服务除外。如：
                    <ul>
                        <li>ISO 9001咨询费用</li>
                        <li>其他外部服务</li>
                     </ul>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        当只有一个部门受益于外部服务时，全部费用应归入该部门。如不止一个部门获益时，应将费用按比例分摊到各部门。其中清洁和保安外包费用不在此科目金额反映。
    </div>
</div>'
WHERE id=21;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">邮资</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映邮件服务相关费用，如邮费，快递等费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	邮票和其他邮递费用 <br>
                    2.	隔夜邮递服务或快递服务费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        除了邮票，邮资计费器的补充费用，欠资数额支付，认证邮件的成本，以及任何其他与邮件服务相关费用记入该账户。 <br>
        此帐户还包括使用隔夜快递运输服务的成本。   <br>

    </div>
</div>'
WHERE id=22;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">差旅费</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映经销商部门经理级别以上人员开展业务所发生的差旅费及业务招待费。本旅费应与员工培训旅费区别开来，员工培训旅费应计入账账户。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1、开展业务相关的宾馆、餐费和娱乐费用
                    2、招待费
                    3、会议费（含董事会费）

                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        总经理和领导层的费用应当在销售和业务部门中按一定比例分摊。<br>
        当一个部门从旅费-招待费用中获益，全部费用归该部门，如不止一个部门获益，则将费用按比例分摊给各部门
    </div>
</div>'
WHERE id=23;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">审计与法律服务费用</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映来自律师和会计师的专业顾问费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	每月应计的法律和会计费用
                    2.	诉讼费和申诉费
                    3.	收集代理服务
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        按实际财务发生为准。     <br>
        当一个部门因支付审计和法律费用受益时，全部费用归入该部门。如不止一个部门获益时，应将费用按比例分摊到各个部门。
    </div>
</div>'
WHERE id=24;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">软件、数据维护费用</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映为经销商提供计算机数据处理及服务支持等发生的费用，包括DMS厂家年费。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	DMS厂家费用
                    2.	数据处理设备的租赁或租用金额
                    3.	数据处理设备的维修费用
                    4.	支付给计算机服务部门的数据处理费用
                    5.	数据处理物资费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        按实际财务发生为准。     <br>
        当一个部门因数据处理受益，全部费用由该部门承担。如多部门受益，应将费用按比例分摊到各部门。
    </div>
</div>'
WHERE id=25;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">捐赠款</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额反映因各种慈善事业给予的慈善捐赠。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	对慈善机构的捐款
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        当只有一个部门进行了赞助，费用全部由该部门承担。如有多个部门发起，应将费用按比例各部门间进行分摊。
    </div>
</div>'
WHERE id=26;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">其它利息及银行手续费</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映应支付或应计的贷款利息或其他银行利息费用，包括客户信用卡费用。该账户不包括经销商通过房产抵押贷款利息和车辆融资利息。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	贷款利息
                    2.	银行评估各项服务的手续费金额，如支票帐户，停止支付等
                    3.	客户采购付款相关的信用卡费用
                    4.	支票验证服务费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        一般利息和银行手续费用应按比例分摊到各部门，除非有一个部门承担了该项费用。  <br>
        当客户使用信用卡购物或支付服务费时，信用卡费也记入该账户。
    </div>
</div>'
WHERE id=27;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">保安、清洁费用</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映支付给提供清洁和安保服务的费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	支付给外部供应商的门卫服务费用
                    2.	支付给外部供应商的清洁服务费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        此费用应根据各部门区域比例和服务相关成本比例进行分摊。
    </div>
</div>'
WHERE id=28;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">通讯费 (电话、传真、网络)</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映经销商的电话、传真、互联网费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	每月的电话、传真及电脑线费用
                    2.	每月电话系统或通话系统租金
                    3.	每月计提的电话费，如每月完毕正常收到电话账单。
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        以财务实际情况为准。     <br>
        当只有一个部门受益于电话-传真-互联网时，全部费用应由该部门承担。如多个部门受益，应将费用按比例分配给各个部门。分配率应基于各部门安装的电话、传真和电脑线数量等进行合理计算。
    </div>
</div>'
WHERE id=29;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">其它 (含安全生产费用)</legend>
    <div>
        <b>所含金额</b><br>
        该科目金额反映不能恰当记入其他费用账户的综合项费用。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	不能分配给其他费用账户的费用或累计费用
                    2.	特殊场合花费
                    3.	安全生产费用等
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        该账户只能在不能记入其他费用账户时使用。因为费用账户范围比较全面，很少使用本账户。只要有可能，该账户费用应归于申请部门或受益部门，如有两个或多个部门涉及，则费用应按比例在各部门分配。
    </div>
</div>'
WHERE id=30;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">公司领导层薪酬</legend>
    <div>
        <b>所含金额</b><br>
        该科目余额表示支付给领导层（包括所有驻在员的费用）的工资总计，不含年终奖。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	应付给公司领导层的工资（不含奖金）
                    2.	驻在员费用
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        经营管理层面的工资应该由公司的最高管理层公平的分配到每个为其做出贡献的部门
    </div>
</div>'
WHERE id=15;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">部门经理薪酬</legend>
    <div>
        <b>所含金额</b><br>
        该账户余额表示已经支付给部门经理和副经理的工资总计（不包含奖金）。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	应付或者支付给经理和主管的工资（不含奖金）
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        主管级别的薪酬一般不计入此科目。  <br>
        经理和副经理工资应该由记入相应部门的费用。 <br>
        负责多个部门的经理应该把工资公平的分摊到自己管辖的各个部门。 分配比例可以按照该经理对每个部门投入的精力和时间来计算。
    </div>
</div>'
WHERE id=16;

UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">福利</legend>
    <div>
        <b>所含金额</b><br>
        该科目余额表示公司给所有员工提供的福利费用，主要包含5金一险及商业险、工会经费、辞退福利、劳动保护费等。<br>
        <table border="1">
            <tr>
                <th>借方</th>
                <th>贷方</th>
            </tr>
            <tr>
                <td>
                    1.	主要有如下项目：
                    <ul>
                        <li>安全保险</li>
                        <li>五险一金</li>
                        <li>员工商业保险</li>
                        <li>子女福利</li>
                        <li>其他的员工福利</li>
                     </ul>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>说明</b>
    <div style="font-size: 12px;color: gray">
        经销店员工的福利应该记入相应部门的费用。服务于多个部门的员工的应该公平的分摊到每个受服务部门。 <br>
        分配比例可以按照该员工对每个部门投入的精力和时间来计算。      <br>
        服务所有部门的成员，比如总经理和领导层，应该分摊到所有部门。
    </div>
</div>'
WHERE id=17;


UPDATE GeneralJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">非正式人员费用</legend>
    <div>
        <b>所含金额</b><br>
        该科目余额表示公司给所有员工提供的福利费用，主要包含5金一险及商业险、工会经费、辞退福利、劳动保护费等。<br>
    </div>
</div>'
WHERE id=18;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">分期收入</legend>
    <div>
        <b>附加产品业务-新车</b><br>
        <b>金额</b><br>
        此账户余额反映从按揭贷款业务所获得的数量、收入、毛利润。 <br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                    通过按揭贷款业务获得的收入
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=6;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">保险收入</legend>
    <div>
        <b>附加产品业务-新车</b><br>
        <b>余额</b><br>
        此金额反映销售部门卖出的各类保险销售的数量、收入、毛利润。 <br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                    通过保单销售获得的保险收入
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>备注</b>
</div>'
WHERE id=7;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">延保收入</legend>
    <div>
        <b>附加产品业务-新车</b><br>
        <b>余额</b><br>
        此账户余额反映新车部门销售给新车客户的延长保修合同的数量、收入、毛利润。 <br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                    销售给新车客户的延长保修合同售价
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>备注</b>
</div>'
WHERE id=8;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">牌照收入</legend>
    <div>
        <b>附加产品业务-新车</b><br>
        <b>余额</b><br>
        此账户余额反映与新车一同销售的上牌业务的销售数量、收入、毛利润。 <br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                    上牌业务的销售收入
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>备注</b>
</div>'
WHERE id=9;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">其他附加收入</legend>
    <div>
        <b>附加产品业务-新车</b><br>
        <b>余额</b><br>
        该账户余额反映新车部门卖出的精品收入、毛利润。 <br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                    精品收入
                </td>
                <td></td>
            </tr>
        </table>
    </div>
    <br>
    <b>备注</b>

    <div style="font-size: 12px;color: gray">
        这里体现的是新车交易时额外售出的精品。如果，精品和新车是整体打包出售，销售部门确实无法分出精品价值，精品成本是集成在新车成本里的，那么不用体现在这里。 <br>
        对于随新车出售的精品的成本，销售部门和配件部门需要讨论出毛利的分摊比例。 <br>
        例如，新车销售部门在某精品上收入1000元，需要返回配件部门精品售价400元，则销售部门在这项精品交易中获利600元，本科目应该填写收入1000，毛利600。 <br>
        同时，配件部门获得精品收入400元，但进货成本为300元，则配件部门在这项精品交易中获利100元，在配件部门下科目523应该填写收入400，毛利100。 <br>
        如果销售部门直接按进货成本300计算其毛利，也可以接受。这种情况下，本次交易就不计入配件部门的科目523了。 <br>
    </div>
</div>'
WHERE id=10;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">认证二手车零售</legend>
    <div>
        <b>二手车部门</b><br>
        <b>余额</b><br>
        该项反映原厂认证的二手车销售业务的数量、收入、毛利润。 <br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=11;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">认证二手车翻新</legend>
    <div>
        <b>二手车部门</b><br>
        <b>余额</b><br>
        此账户余额反映用于零售的原厂认证二手车的翻新成本。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td> 1. 库存记录在案的用于零售的原厂认证二手车翻新成本
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=12;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">非认证二手车零售</legend>
    <div>
        <b>二手车部门</b><br>
        <b>余额</b><br>
        此账户余额反映未认证二手车的销售数量、收入、毛利润。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=13;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">非认证二手车翻新</legend>
    <div>
        <b>二手车部门</b><br>
        <b>余额</b><br>
        此账户余额反映用于零售的非认证二手车发生的翻新成本。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td> 非认证二手车售出前的翻新成本
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=14;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">批发销售</legend>
    <div>
        <b>二手车部门</b><br>
        <b>余额</b><br>
        此账户余额反映二手车批发销售的数量、收入、毛利润。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=15;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">库存调整</legend>
    <div>
        <b>二手车部门</b><br>
        <b>余额</b><br>
        此科目反映二手车部门由于库存的调整而产生的损益。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=16;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">客户付费工时</legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        此科目余额反映客户付费的机修工单的工单数，工时收入和毛利润。工时的成本即为付给维修技师的薪酬（包含底薪和提成）。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>1. 机修工时售价</td>
            </tr>
        </table>
    </div>
    <br>
    <b>备注</b>

    <div style="font-size: 12px;color: gray">
        工时收入应为打折后的实际收费。 <br>
        对于代金券的使用，基本原则是，要有承担这笔费用的部门，并且考虑这笔费用是为了哪项业务发生的。这就要求规范代金券的发放流程和制度，要能够让代金券的最终接收部门了解代金券发放的部门和发放原因。可以将这笔费用按比例进行分摊处理。同时，应该是代金券被使用时，才计入某项费用或成本里。
    </div>
</div>'
WHERE id=17;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">内部工时</legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        此科目余额反映经销商其他部门所提供的维修业务的工单数，维修工时收入和毛利。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>1. 机修工时售价</td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=19;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        精品安装工时
    </legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        此科目余额反映为精品安装的总工时收入。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>为新车和二手车正确安装精品及其附件产生的工时收入</td>
            </tr>
        </table>
    </div>
    <br>
    <b>备注</b>
    <div  style="font-size: 12px;color: gray">
        目前精品安装没有单独体现出工时的收费。
    </div>
</div>'
WHERE id=20;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        内外清洗、细节保养清洁工时
    </legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        此科目用于核算深度美容业务，和细节保养清洁的工单数、工时收入和毛利润。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>内外清洗、细节保养清洁金额（主要指深度美容项目）</td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=21;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        其它工时
    </legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        该项科目余额表示未计入上述帐目的工时。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>未计入上述帐目的工时</td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=22;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        未使用工
    </legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        此项目暂时不使用。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=23;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        未使用工
    </legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        此项目暂时不使用。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=23;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        外修
    </legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        此科目余额反映送到外面的维修单位的工单数、收入和毛利（包括配件和维修工时）。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>1. 销售给客户的外加工售价 <br>
                    2. 为经销商其他部门如二手车部门提供的外加工的售价(特别的翻新修理) <br>
                    3. 厂商保修项下进行的外加工售价 <br>
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=24;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        延保
    </legend>
    <div>
        <b>维修部</b><br>
        <b>余额</b><br>
        该科目余额表示通过维修部售出的延保收入和续保收入之和。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>1、通过维修部售出的延保总销售额 <br>
                    2、通过维修部售出的续保销售收入
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=25;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        客户－维修部
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此科目余额反映用于客户付费的机修工单中的配件销售收入。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>客户付费机修工单中使用的配件销售收入
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=26;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        保修
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此账户余额反映保修工单中的配件销售收入。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>原厂保修工单中的配件销售收入
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=27;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        内部
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此账户余额反映内部维修工单中使用的配件销售额。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td> 1. 从库存车辆上移除并退回配件部的配件售价
                </td>
                <td>1. 内部维修工单中使用的配件销售收入
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=28;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        精品
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此科目余额反映精品的销售额。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>1. 销售给零售客户的精品销售收入
                </td>
            </tr>
        </table>
    </div>
    <br>
    <b>备注</b>

    <div style="font-size: 12px;color: gray">
        目前该科目主要用于统计售后部门销售的精品。但下述情况也应计入该科目。 <br>
        对于随新车出售的精品，销售部门和配件部门需要讨论出毛利的分摊比例。例如，新车销售部门在某精品上收入1000元，需要返回配件部门精品售价400元，则销售部门在这项精品交易中获利600元，新车部门的“其他附件产品收入”一项应该填写收入1000，毛利600。
        <br>
        同时，配件部门获得精品收入400元，但进货成本为300元，则配件部门在这项精品交易中获利100元，在本科目里应该填写收入400，毛利100。 <br>
        如果销售部门直接按进货成本300计算其毛利，也可以接受。这种情况下，本次交易就不计入本科目了。 <br>
    </div>
</div>'
WHERE id=29;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        客户－钣喷
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此科目余额反映客户付费的钣喷工单的钣喷材料和配件销售额。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>1. 钣金车间用于维修的配件销售收入
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=30;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        批发
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此科目余额反映销售给批发商或车队（非终端客户）的原厂配件。目前广丰广本店没有批发业务。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=31;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        柜台零售
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此账户余额反映外卖小商品（除了配件、精品、油品、轮胎等之外的其他商品）的零售额。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td> 1. 外卖小商品的销售额
                    <ul>
                        <li>柜台销售</li>
                        <li>邮购</li>
                    </ul>
                </td>
            </tr>
        </table>
    </div>
    <br>
</div>'
WHERE id=32;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        其它营业外收入
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此账户核算售后部门的废旧件和废机油等废品收入。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>
                </td>
            </tr>
        </table>
    </div>
    <br>
</div>'
WHERE id=33;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        油品
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此科目余额反映机油、变速箱油、刹车油等油品的销售收入。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td> 零售、批发或内部销售汽油，油料和润滑油的销售收入
                </td>
            </tr>
        </table>
    </div>
    <br>
</div>'
WHERE id=34;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        轮胎
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此科目余额反映轮胎的销售额。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td> 由于轮胎退回库存而产生的退款金额
                </td>
                <td> 零售、批发或内部销售轮胎的销售收入
                </td>
            </tr>
        </table>
    </div>
    <br>
</div>'
WHERE id=35;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        订货折扣或其它厂家返利
    </legend>
    <div>
        <b>配件部门</b><br>
        <b>余额</b><br>
        此账户余额反映从OEM收到的配件及精品销售返利。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td> 1. 为采购很难找到的配件而支付给其他经销商的额外费用<br>
                    2. 配件和精品退回给OEM或其他供应商的折扣
                </td>
                <td> 零售、批发或内部销售轮胎的销售收入
                </td>
            </tr>
        </table>

        <b>备注</b>

        <div  style="font-size: 12px;color: gray">
            目前暂时基本没有使用该科目
        </div>
    </div>
</div>'
WHERE id=36;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        客户付费钣喷工时
    </legend>
    <div>
        <b>钣金部门</b><br>
        <b>余额</b><br>
        此科目余额反映客户付费的钣金工单中工时收入金额。注意，不论钣喷是否外包，都在这里体现所有的客户付费钣喷业务。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td> 此科目余额反映客户付费的钣金工单中工时收入金额。注意，不论钣喷是否外包，都在这里体现所有的客户付费钣喷业务。
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=38;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        钣喷保修工时
    </legend>
    <div>
        <b>钣金部门</b><br>
        <b>余额</b><br>
        此科目余额反映原厂保修范围内钣金保修工时收入。注意，不论钣喷是否外包，都在这里体现所有的保修钣喷业务。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td> 为仍在厂商保修范围内的新车进行的钣金保修工时售价。该金额由厂家支付。
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=39;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        钣喷内部工时
    </legend>
    <div>
        <b>钣金部门</b><br>
        <b>余额</b><br>
        此账户余额反映为经销商所有部门进行的内部钣喷工时金额。注意，不论钣喷是否外包，都在这里体现所有的内部钣喷业务。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td> 为其他部门进行的钣喷工时售价。
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=40;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        钣喷未使用/浪费工时
    </legend>
    <div>
        <b>钣金部门</b><br>
        <b>余额</b><br>
        钣喷未使用/浪费工时。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=44;


UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        外修
    </legend>
    <div>
        <b>钣金部门</b><br>
        <b>余额</b><br>
        此科目余额反映外加工钣喷金额。这里仅仅是那些没有外包钣喷的企业，而同时又确实有少量外送钣喷业务的情况。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>
                    1. 销售给客户的外加工钣喷售价 <br>
                    2. 为其他部门进行的外加工钣喷售价 <br>
                    3. 厂商保修项下进行的外加工售价 <br>
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=45;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        钣喷材料收入
    </legend>
    <div>
        <b>钣金部门</b><br>
        <b>余额</b><br>
        此科目余额仅仅是用来反映那些没有外包钣喷业务的企业，其钣金部门在少量的外送的钣喷业务时产生的材料收入。<br>
        <table border="1">
            <tr>
                <th>支出</th>
                <th>收入</th>
            </tr>
            <tr>
                <td>
                </td>
                <td>
                </td>
            </tr>
        </table>
    </div>
</div>'
WHERE id=46;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        大客户采购（租车公司，政府机关）
    </legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映大客户交易的台数，收入及毛利润。<br>
    </div>
</div>'
WHERE id=2;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        二网销售
    </legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映卖给二级网点的交易的台数，收入及毛利润。<br>
    </div>
</div>'
WHERE id=3;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        他店调车
    </legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映将车卖给其他经销商的交易的台数及毛利润。<br>
    </div>
</div>'
WHERE id=4;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        零售销售返利
    </legend>
    <div>
        <b>所含金额</b><br>
        此科目所含金额反映按照本月所销售的车辆（零售、大客户及二网）以及厂家商务政策，应可拿到的销售返利。此科目与财务帐是不一致的，一定不要把以往的返利计入本科目。<br>
    </div>
</div>'
WHERE id=5;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        汽车租赁
    </legend>
    <b>科目金额</b><br>
    该科目金额反映为汽车租赁业务带来的收入及毛利。<br>
    <div>
        <b>备注</b>
        <div  style="font-size: 12px;color: gray">该科目的收入和毛利润将进入第一页的租赁事业部。</div>
    </div>
</div>'
WHERE id=47;

UPDATE SalesServiceJournalItem  
SET tooltip='<div style="width: 500px" >
    <legend style="font-size: 16px">
        其他附加业务
    </legend>
    <b>科目金额</b><br>
    这些由各企业自由添加的项目体现了各企业其他非主业的额外业务所带来的收入和毛利。例如，培训中心，食堂等。<br>
    <div>
        <b>备注</b>
        <div  style="font-size: 12px;color: gray">这些添加的项目的合计将进入其他部门。</div>
    </div>
</div>'
WHERE id=10;