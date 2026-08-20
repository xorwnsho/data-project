function topBreakdown(breakdown, limit = 5) {
	return Object.entries(breakdown ?? {})
		.sort((a, b) => b[1] - a[1])
		.slice(0, limit);
}

export default function StatsSummary({ stats }) {
	const breakdown = topBreakdown(stats.industryBreakdown);
	const maxCount = breakdown.length > 0 ? breakdown[0][1] : 1;

	return (
		<div className="stats-summary">
			<div className="stats-grid">
				<div className="stat-card">
					<span className="stat-label">지역</span>
					<span className="stat-value">{stats.regionName}</span>
				</div>
				<div className="stat-card">
					<span className="stat-label">전체 상가업소 수</span>
					<span className="stat-value">{stats.totalStoreCount.toLocaleString()}개</span>
				</div>
				{stats.industryKeyword && (
					<div className="stat-card">
						<span className="stat-label">{stats.industryKeyword} 경쟁업체 수</span>
						<span className="stat-value">{stats.competitorCount.toLocaleString()}개</span>
					</div>
				)}
			</div>

			<div className="breakdown">
				<h3>업종별 분포 (상위 5)</h3>
				<div className="breakdown-list">
					{breakdown.map(([name, count]) => (
						<div className="breakdown-row" key={name}>
							<span className="breakdown-name" title={name}>{name}</span>
							<div className="breakdown-track">
								<div
									className="breakdown-fill"
									style={{ width: `${Math.max((count / maxCount) * 100, 4)}%` }}
								/>
							</div>
							<span className="breakdown-count">{count}개</span>
						</div>
					))}
				</div>
			</div>
		</div>
	);
}
